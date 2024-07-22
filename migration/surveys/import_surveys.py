import requests
import json
import time

SURVEYS_FILE = 'prod_survey_results.json'
SURVEYS_FAILED_RECORDS_FILE = 'survey_failed_records.json'
URL = 'https://gpp-admin.devel.esaportal.eu/personaldata/rest/surveys'
HEADERS = {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ACCESS_TOKEN'
}

def main():
    start_time = log_start_time()

    data = load_data(SURVEYS_FILE)
    failed_records = process_records(data)

    log_end_time(start_time)

    if failed_records:
        save_failed_records(failed_records, SURVEYS_FAILED_RECORDS_FILE)

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
        response.raise_for_status()  # Throw an error for bad status codes
        print_response_status(response)
        return True
    except requests.exceptions.RequestException as e:
        print(f'Failed to send data: {e}')
        return False

def create_payload(record):
    return {
        "from": record.get('from', ''),
        "classification": record.get('classification', ''),
        "foundLookingFor": record.get('found what looking for', ''),
        "lookingFor": record.get('looking for', ''),
        "searchCriteria": record.get('search_criteria', ''),
        "visualization": record.get('visualization', ''),
        "adequately": record.get('adequately', ''),
        "interest": record.get('interest', ''),
        "organized": record.get('organized', ''),
        "impression": record.get('impression', '')
    }

def print_response_status(response):
    if response.status_code == 201:
        print('Data sent successfully:', response.json())
    else:
        print('Unexpected status code:', response.status_code, response.text)

def save_failed_records(failed_records, file_path):
    try:
        with open(file_path, 'w', encoding='utf-8') as outfile:
            json.dump(failed_records, outfile, ensure_ascii=False, indent=4)
        print(f"Failed records saved to {file_path}")
    except IOError as e:
        print(f"Error saving failed records: {e}")

if __name__ == "__main__":
    main()
