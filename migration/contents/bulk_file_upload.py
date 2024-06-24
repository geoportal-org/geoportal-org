import os
import requests

URL = 'https://gpp-admin.devel.esaportal.eu/contents/rest/document'
FOLDER_PATH = r'FOLDER_PATH'
HEADERS = {
    'Authorization': 'Bearer ACCESS_TOKEN'
}
FOLDER_ID = 130
PATH = '0/130'
SITE_ID = 0

def main():
    for filename in os.listdir(FOLDER_PATH):
        file_path = os.path.join(FOLDER_PATH, filename)
        if os.path.isfile(file_path):
            extension = filename.split('.')[-1]
            files = {
                'files': (filename, open(file_path, 'rb'))
            }

            model = {
                "title": filename,
                "fileName": filename,
                "extension": extension,
                "path": PATH,
                "folderId": FOLDER_ID,
                "siteId": SITE_ID,
            }

            data = {
                'model': str(model).replace("'", '"')
            }

            response = requests.post(URL, headers=HEADERS, files=files, data=data)

            if response.status_code == 201:
                print(f'Successfully uploaded {filename}')
            else:
                print(f'Failed to upload {filename}: {response.status_code} {response.text}')

if __name__ == "__main__":
    main()