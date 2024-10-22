import configparser
import json
import os
import sys
import time

import requests
from keycloak import KeycloakOpenID

DEFAULT_LAYERS_FILE = 'default_layers.json'
DEFAULT_LAYERS_FAILED_RECORDS_FILE = 'default_layers_failed_records.json'
DEFAULT_KML_DIR = 'default_layers_kml'


def main():
    start_time = log_start_time()

    config_file = sys.argv[1] if sys.argv[1:] else 'environment_config.ini'
    print("Read configuration from file:", config_file)
    config = configparser.RawConfigParser()
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
    failed_data_file = os.path.join(failed_data_dir, DEFAULT_LAYERS_FAILED_RECORDS_FILE)
    if os.path.exists(failed_data_file):
        os.remove(failed_data_file)
    # Create folder for kml files
    default_kml_dir = os.path.join(script_dir, DEFAULT_KML_DIR)
    kml_dir = config.get('FS', 'kml_dir', fallback=default_kml_dir).strip('"').strip("'")
    if not os.path.exists(kml_dir):
        os.makedirs(kml_dir)

    # Keycloak configuration
    kc_conf = dict((key, value.strip("\'\"")) for key, value in config.items('KC'))
    keycloak_openid = get_keycloak_openid(kc_conf.get('base_url'))
    admin_access_token = get_admin_access_token(keycloak_openid, kc_conf.get('user_name'), kc_conf.get('user_pass'))

    site_api_url = config.get('contents', 'site_api_url').strip('\'\"')
    folder_api_url = config.get('contents', 'folder_api_url').strip('\'\"')
    document_api_url = config.get('contents', 'document_api_url').strip('\'\"')
    layers_api_url = config.get('settings', 'layers_api_url').strip('\'\"')
    data = load_data(data_dir, DEFAULT_LAYERS_FILE)
    failed_records = process_records(data, admin_access_token, site_api_url, folder_api_url, document_api_url, layers_api_url, kml_dir)

    log_end_time(start_time)

    if failed_records:
        save_failed_records(failed_records, failed_data_dir, DEFAULT_LAYERS_FAILED_RECORDS_FILE)


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


def process_records(data, admin_access_token, site_api_url, folder_api_url, document_api_url, dest_api_url, file_dir):
    failed_records = []
    for record in data:
        if not send_data(record, admin_access_token, site_api_url, folder_api_url, document_api_url, dest_api_url, file_dir):
            failed_records.append(record)
    return failed_records


def send_data(record, admin_access_token, site_api_url, folder_api_url, document_api_url, dest_api_url, file_dir):
    try:
        lf_friendly_url = record.get('friendlyURL', '')
        print(f"friendly_url: {lf_friendly_url}")
        site_id = get_site_id_by_liferay_friendly_url(admin_access_token, site_api_url, lf_friendly_url)
        print(f"site_id: {site_id}")
        headers = create_headers(admin_access_token)
        payload = create_payload(record, site_id, admin_access_token, folder_api_url, document_api_url, file_dir)
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


def get_site_id_by_liferay_friendly_url(access_token, site_api_url, lf_friendly_url):
    if '/global' == lf_friendly_url:
        raise Exception("Ommitted lf_friendly_url " + str(lf_friendly_url))
    elif '/guest' == lf_friendly_url:
        url_param = 'global'
    else:
        url_param = lf_friendly_url.lstrip('/')
    url = site_api_url + '/search/findByUrl?url=' + url_param
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


def create_payload(record, site_id, access_token, folder_api_url, document_api_url, file_dir):
    visible = record.get('visible', '')
    if visible and visible == 1:
        visible = 'true'
    else:
        visible = 'false'
    return {
        "name": record.get('name', ''),
        "url": create_url(record, site_id, access_token, folder_api_url, document_api_url, file_dir),
        "visible": visible,
        "siteId": site_id,
    }


def create_url(record, site_id, access_token, folder_api_url, document_api_url, file_dir):
    url = record.get('url', '')
    print(url)
    file = record.get('file', '')
    if file:
        folder = create_folder(site_id, access_token, folder_api_url)
        file_path = os.path.join(file_dir, file)
        file_url = upload_kml_file(site_id, folder, file_path, access_token, document_api_url)
        print(file_url)
        return file_url
    return url


def create_folder(site_id, access_token, folder_api_url):
    title = 'layers'
    url = folder_api_url + '/search/findByTitleAndSiteId?title=' + title + '&siteId=' + site_id
    headers = create_headers(access_token)
    response = requests.get(url, headers=headers)
    folder_id = None
    if response.status_code == 200:
        folder = response.json()
        if folder:
            folder = folder.get('_embedded', '')
            if folder:
                folder = folder.get('folder', '')
                if folder:
                    folder = folder[0]
                    if folder:
                        folder_id = folder.get('id', '')
                        folder_path = folder.get('path', '')
                        return folder_id, folder_path
    if not folder_id:
        headers = create_headers(access_token)
        payload = create_folder_payload(site_id, title)
        response = requests.post(folder_api_url, headers=headers, json=payload)
        if response.status_code == 201:
            print_response_status(response)
            response_json = response.json()
            folder_id = response_json.get('id', '')
            folder_path = response_json.get('path', '')
            return folder_id, folder_path
        else:
            print(response)
            raise Exception("Failed to create folder layers in site " + str(site_id))


def create_folder_payload(site_id, title):
    return {
        "title": title,
        "parentFolderId": site_id,
        "path": site_id,
        "siteId": site_id,
    }


def upload_kml_file(site_id, folder, file_path, access_token, document_api_url):
    file_name = os.path.basename(file_path)
    files = {
        'files': (file_name, open(file_path, 'rb'))
    }
    folder_id = folder[0]
    folder_path = folder[1] + '/' + str(folder_id)
    model = {
        "title": file_name,
        "fileName": file_name,
        "extension": "kml",
        "path": folder_path,
        "folderId": folder_id,
        "siteId": site_id,
    }
    data = {
        'model': str(model).replace("'", '"')
    }
    headers = {
        'Authorization': 'Bearer ' + access_token
    }
    response = requests.post(document_api_url, headers=headers, files=files, data=data)
    if response.status_code == 201:
        print_response_status(response)
        response_json = response.json()
        file_id = response_json.get('id', '')
        file_url = document_api_url + '/' + str(file_id) + '/content'
        return file_url
    else:
        raise Exception("Failed to create file in site " + str(site_id))


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
