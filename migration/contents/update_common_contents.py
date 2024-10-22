import configparser
import json
import os
import sys
import time

import requests
from keycloak import KeycloakOpenID

CONTENTS_FAILED_RECORDS_FILE = 'contents_common_failed_records.json'
CONTENTS_DIR = 'common'


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
    failed_data_file = os.path.join(failed_data_dir, CONTENTS_FAILED_RECORDS_FILE)
    if os.path.exists(failed_data_file):
        os.remove(failed_data_file)
    # Contents
    contents_dir = os.path.join(data_dir, CONTENTS_DIR)
    if not os.path.exists(contents_dir):
        os.makedirs(contents_dir)

    # Keycloak configuration
    kc_conf = dict((key, value.strip("\'\"")) for key, value in config.items('KC'))
    keycloak_openid = get_keycloak_openid(kc_conf.get('base_url'))
    admin_access_token = get_admin_access_token(keycloak_openid, kc_conf.get('user_name'), kc_conf.get('user_pass'))

    site_api_url = config.get('contents', 'site_api_url').strip('\'\"')
    content_api_url = config.get('contents', 'content_api_url').strip('\'\"')
    sites = get_sites(admin_access_token, site_api_url)
    failed_records = process_sites(sites, admin_access_token, content_api_url, contents_dir)

    log_end_time(start_time)

    if failed_records:
        save_failed_records(failed_records, failed_data_dir, CONTENTS_FAILED_RECORDS_FILE)


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


def get_sites(access_token, site_api_url):
    url = site_api_url
    headers = create_headers(access_token)
    response = requests.get(url, headers=headers)
    if response.status_code != 200:
        raise Exception("Sites not found")
    result = response.json()
    embedded = result.get('_embedded', '')
    sites = embedded.get('site', '')
    if not sites:
        raise Exception("Site not found in response")
    return sites


def process_sites(data, admin_access_token, content_api_url, contents_dir):
    failed_records = []
    for record in data:
        if not process_site(record, admin_access_token, content_api_url, contents_dir):
            failed_records.append(record)
    return failed_records


def process_site(record, admin_access_token, content_api_url, contents_dir):
    site_id = record.get('id', '')
    name = record.get('name', '')
    print("Site:", name, site_id)
    contents = get_contents(admin_access_token, content_api_url, site_id)
    print("Contents:", len(contents))
    process_contents(contents, admin_access_token, content_api_url, contents_dir)
    return True


def get_contents(access_token, content_api_url, site_id):
    url = content_api_url + '/search/findBySiteId?siteId=' + str(site_id)
    headers = create_headers(access_token)
    response = requests.get(url, headers=headers)
    if response.status_code != 200:
        raise Exception("Contents not found")
    result = response.json()
    embedded = result.get('_embedded', '')
    contents = embedded.get('content', '')
    if not contents:
        raise Exception("Contents not found in response")
    return contents


def process_contents(data, admin_access_token, content_api_url, contents_dir):
    failed_records = []
    for record in data:
        if not process_content(record, admin_access_token, content_api_url, contents_dir):
            failed_records.append(record)
    return failed_records


def process_content(record, admin_access_token, content_api_url, contents_dir):
    content_id = record.get('id', '')
    title = record.get('title', '')
    title_en = title.get('en', '')
    print("Content ", content_id, title_en)
    contents_file_name = title_en + ".html"
    contents_file_path = os.path.join(contents_dir, contents_file_name)
    print(contents_file_path)
    if os.path.isfile(contents_file_path):
        with open(contents_file_path, 'r', encoding='utf-8') as contents_file:
            contents_data = contents_file.read()
            data = record.get('data', '')
            for lang in data:
                data[lang] = contents_data
        dest_api_url = content_api_url + '/' + str(content_id)
        send_data(record, admin_access_token, dest_api_url)
    else:
        print("Skip for missing file ", contents_file_path)


def send_data(record, admin_access_token, dest_api_url):
    try:
        headers = create_headers(admin_access_token)
        payload = create_payload(record)
        response = requests.put(dest_api_url, headers=headers, json=payload)
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
    return {
        "title": record.get('title', ''),
        "data": record.get('data', ''),
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
