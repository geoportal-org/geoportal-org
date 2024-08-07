import configparser
import json
import os
import sys

import mysql.connector

LIFERAY_USERS_FILE = 'liferay_users.json'


def main():
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

    # Database connection configuration
    db_config = dict((key, value.strip("\'\"")) for key, value in config.items('DB'))

    # Establish database connection
    cnx = get_db_connection(db_config)
    cursor = cnx.cursor(dictionary=True)

    # Define the SQL query
    query = "SELECT firstName, lastName, screenName AS username, (CASE WHEN status = '0' THEN 'true' ELSE 'false' END) AS enabled, emailAddress AS email, (CASE WHEN emailAddressVerified = '0' THEN 'true' ELSE 'false' END) AS emailVerified, SUBSTRING(languageId, 1, 2) AS locale, userId AS liferay_user_id, password_ AS password FROM User_ WHERE defaultUser != 1"

    # Fetch data from the database
    rows = fetch_data(cursor, query)

    # Save data to JSON file
    save_to_json(rows, data_dir, LIFERAY_USERS_FILE)

    # Close the database connection
    cursor.close()
    cnx.close()


def get_db_connection(config):
    return mysql.connector.connect(**config)


def fetch_data(cursor, query):
    cursor.execute(query)
    return cursor.fetchall()


def save_to_json(data, data_dir, file_name):
    file_path = os.path.join(data_dir, file_name)
    with open(file_path, 'w') as json_file:
        json.dump(data, json_file, indent=4, default=str)


if __name__ == "__main__":
    main()
