import requests
import json
import time

RESOUCE_RATING_ENTRIES_FILE = 'geoss_ResourceRatingEntry.json'
RESOUCE_RATING_ENTRIES_FAILED_RECORDS_FILE = 'resource_rating_entries_failed_records.json'
URL = 'https://gpp-admin.devel.esaportal.eu/curated/rest/rating'
HEADERS = {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ACCESS_TOKEN'
}

def main():
    start_time = log_start_time()

    data = load_data(RESOUCE_RATING_ENTRIES_FILE)
    failed_records = process_records(data)

    log_end_time(start_time)

    if failed_records:
        save_failed_records(failed_records, RESOUCE_RATING_ENTRIES_FAILED_RECORDS_FILE)

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

def process_records(data):
    failed_records = []
    for record in data:
        if not send_data(record):
            failed_records.append(record)
    return failed_records

def send_data(record):
    payload = create_payload(record)
    try:
        response = requests.post(URL, headers=HEADERS, json=payload)
        if response.status_code == 201:
            print_response_status(response)
            return True
        else:
            print('Unexpected status code:', response.status_code, response.text)
            return False
    except requests.exceptions.RequestException as e:
        print(f'Failed to send data: {e}')
        return False

def create_payload(record):
    return {
        "targetId": record.get('targetId', ''),
        "name": record.get('name', ''),
        "groupId": record.get('groupId', ''),
        "createDate": record.get('createDate', ''),
        "modifiedDate": record.get('modifiedDate', ''),
        "score": record.get('score', ''),
        "comment": "",
        "entryXml": record.get('entryXml', ''),
        "dataSource": record.get('dataSource', ''),
        "sourceBaseUrl": record.get('sourceBaseUrl', ''),
        "valid": record.get('valid', '')
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

def save_failed_records(failed_records, file_path):
    try:
        with open(file_path, 'w', encoding='utf-8') as outfile:
            json.dump(failed_records, outfile, ensure_ascii=False, indent=4)
        print(f"Failed records saved to {file_path}")
    except IOError as e:
        print(f"Error saving failed records: {e}")

if __name__ == "__main__":
    main()
