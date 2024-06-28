import mysql.connector
import json


def main():
    # Database connection configuration
    config = {
        'user': 'DB_USER',
        'password': 'DB_PASSWORD',
        'host': 'DB_HOST',
        'database': 'DB_NAME'
    }

    # Establish database connection
    cnx = get_db_connection(config)
    cursor = cnx.cursor(dictionary=True)

    # Define the SQL query
    query = "select * from geoss_TutorialConfig"

    # Fetch data from the database
    rows = fetch_data(cursor, query)

    # Save data to JSON file
    save_to_json(rows, 'tutorial_tags.json')

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
