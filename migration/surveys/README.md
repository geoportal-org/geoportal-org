Install Python 3.12 and check version
[Python Download Page](https://www.python.org)
```sh
python --version
```
Install the package manager pip and check version
```sh
pip --version
```
Install the additional library requests using pip
```sh
pip install requests
```
Download the current production data file prod_survey_results.txt from Google Docs and place it in the script folder.
Google Drive username: geoportal2x@gmail.com, password: syspass.
Change the URL in the script to the API for the specific environment. For DEV, the URL is:
```sh
url = 'https://gpp-admin.devel.esaportal.eu/personaldata/rest/surveys'
```
Generate an ACCESS_TOKEN and place it in the script in the header:
```sh
'Authorization': 'Bearer ACCESS_TOKEN'
```
Run the script
```sh
python import_surveys.py
```
The script processing is finished when the message 'Total execution time' appears.
If there are any problems with uploading records, failed attempts will be stored in the failed_records.json file.
In case of failed records, they will be reviewed and manually added via the API after analysis.

The prod_survey_results.txt data file needs to be archived.