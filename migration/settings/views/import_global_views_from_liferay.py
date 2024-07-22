import json
import time

import requests
from keycloak import KeycloakAdmin
from keycloak import KeycloakOpenID
from keycloak import KeycloakOpenIDConnection

GLOBAL_VIEWS_FILE = 'global_views.json'
GLOBAL_VIEWS_FAILED_RECORDS_FILE = 'global_views_failed_records.json'
API_URL = 'https://gpp.devel.esaportal.eu/settings/rest/views'
SITE_API_URL = 'https://gpp.devel.esaportal.eu/contents/rest/site'
KC_BASE_URL = 'https://gpp-idp.devel.esaportal.eu'
KC_USER_NAME = 'geoss'
KC_USER_PASS = ''


def main():
    start_time = log_start_time()

    keycloak_admin = get_keycloak_admin()
    keycloak_openid = get_keycloak_openid()
    admin_access_token = get_admin_access_token(keycloak_openid)

    data = load_data(GLOBAL_VIEWS_FILE)
    failed_records = process_records(data, keycloak_admin, keycloak_openid, admin_access_token)

    log_end_time(start_time)

    if failed_records:
        save_failed_records(failed_records, GLOBAL_VIEWS_FAILED_RECORDS_FILE)


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
        lf_friendly_url = record.get('friendlyURL', '')
        print(f"friendly_url: {lf_friendly_url}")
        site_id = get_site_id_by_liferay_friendly_url(admin_access_token, lf_friendly_url)
        print(f"site_id: {site_id}")
        headers = create_headers(admin_access_token)
        payload = create_payload(record, site_id)
        response = requests.post(API_URL, headers=headers, json=payload)
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


def get_site_id_by_liferay_friendly_url(access_token, lf_friendly_url):
    if '/global' == lf_friendly_url:
        raise Exception("Ommitted lf_friendly_url " + str(lf_friendly_url))
    elif '/guest' == lf_friendly_url:
        url_param = 'global'
    else:
        url_param = lf_friendly_url.lstrip('/')
    url = SITE_API_URL + '/search/findByUrl?url=' + url_param
    headers = create_headers(access_token)
    response = requests.get(url, headers=headers)
    if response.status_code != 200:
        raise Exception("Site not found for liferay friendly url " + str(lf_friendly_url))
    site = response.json()
    site_id = str(site.get('id', ''))
    if not site_id:
        raise Exception("Site not found for liferay friendly url " + str(lf_friendly_url))
    return site_id


def create_headers(access_token):
    return {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + access_token
    }


def create_payload(record, site_id):
    defaultOption = record.get('defaultOption', '')
    if defaultOption and defaultOption == 1:
        defaultOption = 'true'
    else:
        defaultOption = 'false'
    return {
        "label": record.get('label', ''),
        "title": record.get('title', ''),
        "value": record.get('optionValue', ''),
        "defaultOption": defaultOption,
        "siteId": site_id,
        "subOptions": create_suboptions(record),
    }


def create_suboptions(record):
    options = []
    sub_options = record.get('subOptions', '')
    if sub_options:
        for sub_option in sub_options:
            option = {
                "label": sub_option.get('label', ''),
                "title": sub_option.get('title', ''),
                "value": sub_option.get('optionValue', ''),
            }
            options.append(option)
    return options


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
