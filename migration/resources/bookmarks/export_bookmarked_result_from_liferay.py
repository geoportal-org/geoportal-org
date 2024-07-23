import configparser
import json
import sys

import mysql.connector

BOOKMARKS_FILE = 'geoss_BookmarkedResult.json'


def main():
    config_file = sys.argv[1] if sys.argv[1:] else 'environment_config.ini'
    print("Read configuration from file:", config_file)
    config = configparser.ConfigParser()
    config.read(config_file)
    print("Read configuration sections:", config.sections())

    # Database connection configuration
    db_config = dict((key, value.strip("\'\"")) for key, value in config.items('DB'))

    # Establish database connection
    cnx = get_db_connection(db_config)
    cursor = cnx.cursor(dictionary=True)

    # Define the SQL query
    query = "SELECT * FROM geoss_BookmarkedResult"

    # Fetch data from the database
    rows = fetch_data(cursor, query)

    # Save data to JSON file
    save_to_json(rows, BOOKMARKS_FILE)

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


if __name__ == "__main__":
    main()
