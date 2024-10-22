import configparser
import json
import os
import sys
import time

import requests
from keycloak import KeycloakOpenID

GEOSS_FILES_FILE = 'geoss_files.json'
GEOSS_FILES_FAILED_RECORDS_FILE = 'geoss_files_failed_records.json'
GEOSS_FILES_DIR = 'geoss_files'


def main():
    start_time = log_start_time()

    config_file = sys.argv[1] if sys.argv[1:] else 'environment_config.ini'
    print("Read configuration from file:", config_file)
    config = configparser.RawConfigParser()
    config.read(config_file)
    print("Read configuration sections:", config.sections())

    # storage configuration
    script_dir = os.path.dirname(os.path.abspath(__file__))
    data_dir = script_dir
    failed_data_dir = config.get('FS', 'failed_data_dir', fallback=script_dir).strip('"').strip("'")
    if not os.path.exists(failed_data_dir):
        os.makedirs(failed_data_dir)
    failed_data_file = os.path.join(failed_data_dir, GEOSS_FILES_FAILED_RECORDS_FILE)
    if os.path.exists(failed_data_file):
        os.remove(failed_data_file)
    # Files
    files_dir = os.path.join(data_dir, GEOSS_FILES_DIR)
    if not os.path.exists(files_dir):
        os.makedirs(files_dir)

    # Keycloak configuration
    kc_conf = dict((key, value.strip("\'\"")) for key, value in config.items('KC'))
    keycloak_openid = get_keycloak_openid(kc_conf.get('base_url'))
    admin_access_token = get_admin_access_token(keycloak_openid, kc_conf.get('user_name'), kc_conf.get('user_pass'))

    document_api_url = config.get('contents', 'document_api_url').strip('\'\"')
    data = load_data(data_dir, GEOSS_FILES_FILE)
    failed_records = process_records(data, admin_access_token, document_api_url, files_dir)

    log_end_time(start_time)

    if failed_records:
        save_failed_records(failed_records, failed_data_dir, GEOSS_FILES_FAILED_RECORDS_FILE)


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


def process_records(data, admin_access_token, dest_api_url, data_dir):
    failed_records = []
    for record in data:
        if not send_data(record, admin_access_token, dest_api_url, data_dir):
            failed_records.append(record)
    return failed_records


def send_data(record, admin_access_token, dest_api_url, data_dir):
    try:
        headers = create_headers(admin_access_token)
        payload = create_payload(record)
        data = {
            'model': str(payload).replace("'", '"')
        }
        file_name = record.get('file', '')
        file_path = os.path.join(data_dir, record.get('file', ''))
        files = {
            'files': (file_name, open(file_path, 'rb'))
        }
        response = requests.post(dest_api_url, headers=headers, data=data, files=files)
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
        'Authorization': 'Bearer ' + access_token
    }


def create_payload(record):
    extension = os.path.splitext(record.get('name', ''))[1][1:]
    return {
        "title": record.get('name', ''),
        "fileName": record.get('name', ''),
        "extension": extension,
        "path": '0',
        "folderId": 0,
        "siteId": 0
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
