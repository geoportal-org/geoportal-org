Install Python 3.12 and check version
[Python Download Page](https://www.python.org)
```sh
python --version
```
Install the package manager pip and check version
```sh
pip --version
```
Install the additional library mysql-connector-python and python-keycloak using pip
```sh
pip install mysql-connector-python
pip install python-keycloak
```

Configure database connection in the script `export_tutorial_tags_from_liferay.py`
```python
'user': 'DB_USER',
'password': 'DB_PASSWORD',
'host': 'DB_HOST',
'database': 'DB_NAME'
```
Run the script
```sh
python export_tutorial_tags_from_liferay.py
```

A file with the exported data `tutorial_tags.json` should be created.

Change the configuration in the `import_tutorial_tags_from_liferay.py` script to the API for the specific environment.
For DEV, the configuration is:
```sh
API_URL = 'https://gpp-admin.devel.esaportal.eu/settings/rest/tags'
KC_BASE_URL = 'https://gpp-idp.devel.esaportal.eu'
KC_USER_NAME = 'geoss'
KC_USER_PASS = '*****'
```

Run the script
```sh
python import_tutorial_tags_from_liferay.py
```

The script processing is finished when the message 'Total execution time' appears.
If there are any problems with uploading records, failed attempts will be stored in the `tutorial_tags_failed_records.json` file.
In case of failed records, they will be reviewed and manually added via the API after analysis.

The `tutorial_tags.json` data file needs to be archived.
