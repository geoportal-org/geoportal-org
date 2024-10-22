import configparser
import json
import os
import sys
import time

import requests
from keycloak import KeycloakOpenID

CUSTOM_CONTENTS_FILE = 'custom_contents.json'
CUSTOM_CONTENTS_FAILED_RECORDS_FILE = 'contents_custom_failed_records.json'
CUSTOM_CONTENTS_DIR = 'contents'


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
    failed_data_file = os.path.join(failed_data_dir, CUSTOM_CONTENTS_FAILED_RECORDS_FILE)
    if os.path.exists(failed_data_file):
        os.remove(failed_data_file)
    # Contents
    contents_dir = os.path.join(data_dir, CUSTOM_CONTENTS_DIR)
    if not os.path.exists(contents_dir):
        os.makedirs(contents_dir)

    # Keycloak configuration
    kc_conf = dict((key, value.strip("\'\"")) for key, value in config.items('KC'))
    keycloak_openid = get_keycloak_openid(kc_conf.get('base_url'))
    admin_access_token = get_admin_access_token(keycloak_openid, kc_conf.get('user_name'), kc_conf.get('user_pass'))

    site_api_url = config.get('contents', 'site_api_url').strip('\'\"')
    content_api_url = config.get('contents', 'content_api_url').strip('\'\"')
    data = load_data(data_dir, CUSTOM_CONTENTS_FILE)
    failed_records = process_records(data, admin_access_token, site_api_url, content_api_url, contents_dir)

    log_end_time(start_time)

    if failed_records:
        save_failed_records(failed_records, failed_data_dir, CUSTOM_CONTENTS_FAILED_RECORDS_FILE)


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


def process_records(data, admin_access_token, site_api_url, content_api_url, data_dir):
    failed_records = []
    for record in data:
        if not send_data(record, admin_access_token, site_api_url, content_api_url, data_dir):
            failed_records.append(record)
    return failed_records


def send_data(record, admin_access_token, site_api_url, content_api_url, file_dir):
    try:
        site_url = record.get('site', '')
        print("siteUrl:", site_url)
        site_id = get_site_id_by_friendly_url(admin_access_token, site_api_url, site_url)
        print("siteId:", site_id)
        name = record.get('name', '')
        content = get_content(admin_access_token, content_api_url, site_id, name)
        file_name = record.get('file', '')
        content_file = get_content_file(file_dir, file_name)
        headers = create_headers(admin_access_token)
        payload = create_payload(content, content_file)
        content_id = content.get('id', '')
        dest_api_url = content_api_url + '/' + str(content_id)
        response = requests.put(dest_api_url, headers=headers, json=payload)
        if response.status_code == 200:
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


def get_site_id_by_friendly_url(access_token, site_api_url, friendly_url):
    url = site_api_url + '/search/findByUrl?url=' + friendly_url
    headers = create_headers(access_token)
    response = requests.get(url, headers=headers)
    if response.status_code != 200:
        raise Exception("Site not found for liferay friendly url " + str(friendly_url))
    site = response.json()
    site_id = str(site.get('id', ''))
    if not site_id:
        raise Exception("Site not found for liferay friendly url " + str(friendly_url))
    return site_id


def get_content(access_token, content_api_url, site_id, title):
    url = content_api_url + '/search/findByTitleAndSiteId?title=' + title + '&siteId=' + site_id
    headers = create_headers(access_token)
    response = requests.get(url, headers=headers)
    if response.status_code != 200:
        raise Exception("Content " + str(title) + " not found in site " + str(site_id))
    content = response.json()
    if not content:
        raise Exception("Content " + str(title) + " not found in site " + str(site_id))
    return content


def get_content_file(file_dir, file_name):
    file_path = os.path.join(file_dir, file_name)
    print(file_path)
    with open(file_path, 'r', encoding='utf-8') as file:
        data = file.read()
        return data


def create_headers(access_token):
    return {
        'Authorization': 'Bearer ' + access_token
    }


def create_payload(record, contents_data):
    data = record.get('data', '')
    for lang in data:
        data[lang] = contents_data
    return {
        "title": record.get('title', ''),
        "data": data,
        "published": record.get('published', ''),
        "siteId": record.get('siteId', '')
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
