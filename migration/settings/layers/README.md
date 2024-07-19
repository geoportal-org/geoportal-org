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

Configure database connection in the script export_default_layers_from_liferay.py
```python
'user': 'DB_USER',
'password': 'DB_PASSWORD',
'host': 'DB_HOST',
'database': 'DB_NAME'
```
Configure liferay domain in the script export_default_layers_from_liferay.py
For DEV, the configuration is:
```python
LF_BASE_URL = 'https://geoss.devel.esaportal.eu/'
```

Run the script
```sh
python export_default_layers_from_liferay.py
```

A file with the exported data default_layers.json should be created.
A directory default_layers_kml with the downloaded kml files from liferay should be created.


Change the configuration in the import_default_layers_from_liferay.py script to the API for the specific environment.
For DEV, the configuration is:
```sh
SITE_API_URL = 'https://gpp-admin.devel.esaportal.eu/contents/rest/site'
FOLDER_API_URL = 'https://gpp-admin.devel.esaportal.eu/contents/rest/folder'
DOCUMENT_API_URL = 'https://gpp-admin.devel.esaportal.eu/contents/rest/document'
API_URL = 'https://gpp-admin.devel.esaportal.eu/settings/rest/layers'
KC_BASE_URL = 'https://gpp-idp.devel.esaportal.eu'
KC_USER_NAME = 'geoss'
KC_USER_PASS = '*****'
```

Run the script
```sh
python import_default_layers_from_liferay.py
```

This script creates folder ‘layers’ and put in them the kml files downloaded from liferay.

The script processing is finished when the message 'Total execution time' appears.
If there are any problems with uploading records, failed attempts will be stored in the failed_records.json file.
In case of failed records, they will be reviewed and manually added via the API after analysis.

The default_layers.json data file with default_layers_kml needs to be archived.
