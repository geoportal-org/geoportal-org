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
    query = """select so.*, g.groupId, g.name, g.friendlyURL
    from geoss_SearchEngine se
    left join geoss_SearchEngineQueryParam qp on qp.searchEngineId = se.searchEngineId
    left join geoss_SearchEngineSelectOption so on so.searchEngineQueryParamId = qp.searchEngineQueryParamId
    inner join Group_ g on qp.groupId = g.groupId
    where qp.optionListName = 'views'
    and so.globalOption = 1 and so.visible = 1
    and so.parentSearchEngineSelectOptionId = 0"""

    # Fetch data from the database
    rows = fetch_data(cursor, query)
    for row in rows:
        search_engine_select_option_id = row.get("searchEngineSelectOptionId")
        sub_options_query = """select * from geoss_SearchEngineSelectOption so
        where so.globalOption = 1 and so.visible = 1
        and so.parentSearchEngineSelectOptionId = """ + str(search_engine_select_option_id)
        sub_options_rows = fetch_data(cursor, sub_options_query)
        row['subOptions'] = sub_options_rows

    # Save data to JSON file
    save_to_json(rows, 'global_views.json')

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
