import json
import time

import requests
from keycloak import KeycloakAdmin
from keycloak import KeycloakOpenID
from keycloak import KeycloakOpenIDConnection

KC_BASE_URL = 'http://geoss-keycloak:8080'
# KC_BASE_URL = 'https://gpp-idp.devel.esaportal.eu'
KC_USER_NAME = 'geoss'
KC_USER_PASS = 'geoss'


def main():
    start_time = log_start_time()

    keycloak_admin = get_keycloak_admin()
    keycloak_openid = get_keycloak_openid()
    admin_access_token = get_admin_access_token(keycloak_openid)

    data = load_data('liferay_users.json')
    failed_records = process_records(data, keycloak_admin, keycloak_openid, admin_access_token)

    log_end_time(start_time)

    if failed_records:
        save_failed_records(failed_records, 'failed_records.json')


def get_keycloak_openid():
    keycloak_openid = KeycloakOpenID(
        server_url=KC_BASE_URL,
        realm_name="geoss",
        client_id="geoss-ui"
    )
    return keycloak_openid


def get_admin_access_token(keycloak_openid):
    token = keycloak_openid.token(
        KC_USER_NAME,
        KC_USER_PASS,
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


def get_keycloak_admin():
    keycloak_connection = KeycloakOpenIDConnection(
        server_url=KC_BASE_URL,
        username=KC_USER_NAME,
        password=KC_USER_PASS,
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


def load_data(file_path):
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            return json.load(file)
    except (FileNotFoundError, json.JSONDecodeError) as e:
        print(f"Error loading data from {file_path}: {e}")
        return []


def process_records(data, keycloak_admin, keycloak_openid, admin_access_token):
    failed_records = []
    for record in data:
        if not send_data(record, keycloak_admin, keycloak_openid, admin_access_token):
            failed_records.append(record)
    return failed_records


def send_data(record, keycloak_admin, keycloak_openid, admin_access_token):
    try:
        lf_user_id = record.get('liferay_user_id', '')
        print(f"lf_user_id: {lf_user_id}")
        payload = create_payload(record)
        response = keycloak_admin.create_user(payload)
        if response:
            print(f"kc_user_id: {response}")
            return True
        else:
            print('Unexpected response:', response)
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
        "firstName": record.get('firstName', ''),
        "lastName": record.get('lastName', ''),
        "username": record.get('username', ''),
        "enabled": record.get('enabled', ''),
        "email": record.get('email', ''),
        "emailVerified": record.get('emailVerified', ''),
        "attributes": create_attributes(record),
        "credentials": create_credentials(record),
        "groups": create_groups(record),
    }


def create_attributes(record):
    return {
        "locale": [record.get('locale', '')],
        "liferay_user_id": [record.get('liferay_user_id', '')]
    }


def create_credentials(record):
    credentials = []
    credential_data = {
        "algorithm": "liferay-sha1",
        "hashIterations": 0
    }
    secret_data = {
        "value": record.get('password', ''),
        "salt": None
    }
    credential = {
        "type": "password",
        "userLabel": "Liferay import",
        "credentialData": json.dumps(credential_data),
        "secretData": json.dumps(secret_data)
    }
    credentials.append(credential)
    return credentials


def create_groups(record):
    groups = ["registered-user"]
    return groups


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


def save_failed_records(failed_records, file_path):
    try:
        with open(file_path, 'w', encoding='utf-8') as outfile:
            json.dump(failed_records, outfile, ensure_ascii=False, indent=4)
        print(f"Failed records saved to {file_path}")
    except IOError as e:
        print(f"Error saving failed records: {e}")


if __name__ == "__main__":
    main()
