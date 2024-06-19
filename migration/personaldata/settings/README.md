Install Python 3.12 and check version
[Python Download Page](https://www.python.org)
```sh
python --version
```
Install the package manager pip and check version
```sh
pip --version
```
Install the additional library mysql-connector-python using pip
```sh
pip install mysql-connector-python
```

Configure database connection in the script export_users_settings_from_liferay.py
```python
'user': 'DB_USER',
'password': 'DB_PASSWORD',
'host': 'DB_HOST',
'database': 'DB_NAME'
```
Run the script
```sh
python export_users_settings_from_liferay.py
```

A file with the exported data users_settings.json should be created.

Change the URL in the import_users_settings.py script to the API for the specific environment. For DEV, the URL is:
```sh
url = 'https://gpp-admin.devel.esaportal.eu/personaldata/rest/settings'
```

Generate an ACCESS_TOKEN and place it in the script in the header:
```sh
'Authorization': 'Bearer ACCESS_TOKEN'
```

Run the script
```sh
python import_users_settings_from_liferay.py
```

The script processing is finished when the message 'Total execution time' appears.
If there are any problems with uploading records, failed attempts will be stored in the failed_records.json file.
In case of failed records, they will be reviewed and manually added via the API after analysis.

The users_settings.json data file needs to be archived.
