import os

import mysql.connector
import json
import urllib.request

import requests

LF_BASE_URL = 'https://geoss.devel.esaportal.eu/'
KML_DIR = 'default_layers_kml'

def main():
    # Database connection configuration
    config = {
        'user': 'DB_USER',
        'password': 'DB_PASSWORD',
        'host': 'DB_HOST',
        'database': 'DB_NAME'
    }

    # Create folder for kml files
    kml_dir_exists = os.path.exists(KML_DIR)
    if not kml_dir_exists:
        os.mkdir(KML_DIR)

    # Establish database connection
    cnx = get_db_connection(config)
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
        file_path = download_kml_file(url, KML_DIR)
        row['file'] = file_path

    # Save data to JSON file
    save_to_json(rows, 'default_layers.json')

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


def download_kml_file(url, dest_dir):
    print(url)
    file_name = None
    if url.startswith('/geoss-portlet/resources/'):
        parts = url.rsplit('/', maxsplit=1)
        file_name = parts[-1]
        url = LF_BASE_URL + url
    elif url.startswith('https://geoss.devel.esaportal.eu/documents'):
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
