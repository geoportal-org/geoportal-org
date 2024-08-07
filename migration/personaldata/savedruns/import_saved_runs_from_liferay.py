import configparser
import json
import os
import sys
import time

import requests
from keycloak import KeycloakAdmin
from keycloak import KeycloakOpenID
from keycloak import KeycloakOpenIDConnection

SAVED_RUNS_FILE = 'saved_runs.json'
SAVED_RUNS_FAILED_RECORDS_FILE = 'saved_runs_failed_records.json'


def main():
    start_time = log_start_time()

    config_file = sys.argv[1] if sys.argv[1:] else 'environment_config.ini'
    print("Read configuration from file:", config_file)
    config = configparser.ConfigParser()
    config.read(config_file)
    print("Read configuration sections:", config.sections())

    # storage configuration
    script_dir = os.path.dirname(os.path.abspath(__file__))
    data_dir = config.get('FS', 'data_dir', fallback=script_dir).strip('"').strip("'")
    if not os.path.exists(data_dir):
        os.makedirs(data_dir)
    failed_data_dir = config.get('FS', 'failed_data_dir', fallback=script_dir).strip('"').strip("'")
    if not os.path.exists(failed_data_dir):
        os.makedirs(failed_data_dir)
    failed_data_file = os.path.join(failed_data_dir, SAVED_RUNS_FAILED_RECORDS_FILE)
    if os.path.exists(failed_data_file):
        os.remove(failed_data_file)

    # Keycloak configuration
    kc_conf = dict((key, value.strip("\'\"")) for key, value in config.items('KC'))
    keycloak_admin = get_keycloak_admin(kc_conf.get('base_url'), kc_conf.get('user_name'), kc_conf.get('user_pass'))
    keycloak_openid = get_keycloak_openid(kc_conf.get('base_url'))
    admin_access_token = get_admin_access_token(keycloak_openid, kc_conf.get('user_name'), kc_conf.get('user_pass'))

    saved_runs_api_url = config.get('personaldata', 'saved_runs_api_url').strip('\'\"')
    data = load_data(data_dir, SAVED_RUNS_FILE)
    failed_records = process_records(data, keycloak_admin, keycloak_openid, admin_access_token, saved_runs_api_url)

    log_end_time(start_time)

    if failed_records:
        save_failed_records(failed_records, failed_data_dir, SAVED_RUNS_FAILED_RECORDS_FILE)


def get_keycloak_openid(kc_base_url):
    keycloak_openid = KeycloakOpenID(
        server_url=kc_base_url,
        realm_name="geoss",
        client_id="geoss-ui"
    )
    return keycloak_openid


def get_admin_access_token(keycloak_openid, kc_user_name, kc_user_pass):
    token = keycloak_openid.token(
        kc_user_name,
        kc_user_pass,
        scope="openid profile roles"
    )
    return token['access_token']


def get_impersonation_access_token(keycloak_openid, admin_access_token, impersonation_user_id):
    impersonation_token = keycloak_openid.exchange_token(
        token=admin_access_token,
        subject=impersonation_user_id,
        subject_token_type="urn:ietf:params:oauth:token-type:access_token",
        requested_token_type="urn:ietf:params:oauth:token-type:access_token",
        scope="openid profile roles"
    )
    return impersonation_token['access_token']


def get_user_info(keycloak_openid, access_token):
    user_info = keycloak_openid.userinfo(access_token)
    return user_info


def get_keycloak_admin(kc_base_url, kc_user_name, kc_user_pass):
    keycloak_connection = KeycloakOpenIDConnection(
        server_url=kc_base_url,
        username=kc_user_name,
        password=kc_user_pass,
        realm_name="geoss",
        user_realm_name="geoss",
        client_id="admin-cli",
        verify=True)
    keycloak_admin = KeycloakAdmin(connection=keycloak_connection)
    return keycloak_admin


def get_user_id_by_liferay_user_id(keycloak_admin, liferay_user_id):
    users = keycloak_admin.get_users(query={
        "max": 1,
        "q": "liferay_user_id:" + str(liferay_user_id)
    })
    if not users:
        raise Exception("User not found for liferay user id " + str(liferay_user_id))
    user = users[0]
    user_id = user.get("id")
    return user_id


def log_start_time():
    start_time = time.time()
    print(f"Script started at: {time.ctime(start_time)}")
    return start_time


def log_end_time(start_time):
    end_time = time.time()
    print(f"Script ended at: {time.ctime(end_time)}")
    print(f"Total execution time: {end_time - start_time:.2f} seconds")


def load_data(data_dir, file_name):
    file_path = os.path.join(data_dir, file_name)
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            return json.load(file)
    except (FileNotFoundError, json.JSONDecodeError) as e:
        print(f"Error loading data from {file_path}: {e}")
        return []


def process_records(data, keycloak_admin, keycloak_openid, admin_access_token, dest_api_url):
    failed_records = []
    for record in data:
        if not send_data(record, keycloak_admin, keycloak_openid, admin_access_token, dest_api_url):
            failed_records.append(record)
    return failed_records


def send_data(record, keycloak_admin, keycloak_openid, admin_access_token, dest_api_url):
    try:
        lf_user_id = record.get('userId', '')
        print(f"lf_user_id: {lf_user_id}")
        user_id = get_user_id_by_liferay_user_id(keycloak_admin, lf_user_id)
        print(f"user_id: {user_id}")
        impersonation_access_token = get_impersonation_access_token(keycloak_openid, admin_access_token, user_id)
        headers = create_headers(impersonation_access_token)
        payload = create_payload(record)
        response = requests.post(dest_api_url, headers=headers, json=payload)
        if response.status_code == 201:
            print_response_status(response)
            return True
        else:
            print('Unexpected status code:', response.status_code, response.text)
            return False
    except requests.exceptions.RequestException as e:
        print(f'Failed to send data: {e}')
        return False
    except Exception as e:
        print(f'Failed to send data: {e}')
        return False


def create_headers(access_token):
    return {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + access_token
    }


def create_payload(record):
    return {
        "name": record.get('name', ''),
        "runId": record.get('runId', ''),
        "workflowId": record.get('workflowId', ''),
        "path": record.get('path', ''),
    }


def print_response_status(response):
    try:
        if response.content:
            response_json = response.json()
            print('Data sent successfully:', response_json)
        else:
            print('Data sent successfully, but no response content.')
    except json.JSONDecodeError:
        print('Failed to decode JSON response')
        print('Status code:', response.status_code)
        print('Response content:', response.content)


def save_failed_records(failed_records, data_dir, file_name):
    file_path = os.path.join(data_dir, file_name)
    try:
        with open(file_path, 'w', encoding='utf-8') as outfile:
            json.dump(failed_records, outfile, ensure_ascii=False, indent=4)
        print(f"Failed records saved to {file_path}")
    except IOError as e:
        print(f"Error saving failed records: {e}")


if __name__ == "__main__":
    main()
