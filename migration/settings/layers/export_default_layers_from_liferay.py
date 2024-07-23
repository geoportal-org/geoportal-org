import configparser
import json
import os
import sys
import urllib.request

import mysql.connector
import requests

DEFAULT_LAYERS_FILE = 'default_layers.json'
KML_DIR = 'default_layers_kml'


def main():
    config_file = sys.argv[1] if sys.argv[1:] else 'environment_config.ini'
    print("Read configuration from file:", config_file)
    config = configparser.ConfigParser()
    config.read(config_file)
    print("Read configuration sections:", config.sections())

    # Database connection configuration
    db_config = dict((key, value.strip("\'\"")) for key, value in config.items('DB'))

    lf_base_url = config.get('LF', 'base_url').strip('\'\"')
    # Create folder for kml files
    kml_dir_exists = os.path.exists(KML_DIR)
    if not kml_dir_exists:
        os.mkdir(KML_DIR)

    # Establish database connection
    cnx = get_db_connection(db_config)
    cursor = cnx.cursor(dictionary=True)

    # Define the SQL query
    query = """select sel.*, g.groupId, g.name, g.friendlyURL
    from geoss_SearchEngine se
    right join geoss_SearchEngineLayers sel on sel.searchEngineId = se.searchEngineId
    inner join Group_ g on g.groupId = se.groupId"""

    # Fetch data from the database
    rows = fetch_data(cursor, query)
    for row in rows:
        url = row.get("url")
        # Get kml file from LF
        file_path = download_kml_file(url, KML_DIR, lf_base_url)
        row['file'] = file_path

    # Save data to JSON file
    save_to_json(rows, DEFAULT_LAYERS_FILE)

    # Close the database connection
    cursor.close()
    cnx.close()


def get_db_connection(config):
    return mysql.connector.connect(**config)


def fetch_data(cursor, query):
    cursor.execute(query)
    return cursor.fetchall()


def save_to_json(data, file_path):
    with open(file_path, 'w') as json_file:
        json.dump(data, json_file, indent=4, default=str)


def download_kml_file(url, dest_dir, lf_base_url):
    print(url)
    file_name = None
    if url.startswith('/geoss-portlet/resources/'):
        parts = url.rsplit('/', maxsplit=1)
        file_name = parts[-1]
        url = lf_base_url + url
    elif url.startswith(lf_base_url + 'documents'):
        parts = url.rsplit('/', maxsplit=2)
        file_name = parts[-2]
    if file_name:
        file_path = dest_dir + '/' + file_name
        download_file(url, file_path)
        return file_path


def download_file(url, file_name):
    try:
        response = urllib.request.urlretrieve(url, filename=file_name)
        if response[1]:
            print('Download file successfully:', response[0])
            return True
        else:
            print('Unexpected response:', response)
            return False
    except requests.exceptions.RequestException as e:
        print(f'Failed to download file: {e}')
        return False
    except Exception as e:
        print(f'Failed to download file: {e}')
        return False


if __name__ == "__main__":
    main()
