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

Create configuration file `environment_config.ini` for scripts that exports data.
Configure database connection in the configuration file
```ini
[DB]
host = "DB_HOST"
port = "DB_PORT"
database = "DB_NAME"
user = "DB_USER"
password = "DB_PASSWORD"
```
Configure liferay's domain in the configuration file
```ini
[LF]
BASE_URL = 'https://LIFERY_DOMAIN/'
```

For DEV, the example configuration is below:
```ini
[DB]
host = "10.254.2.196"
port = "3306"
database = "geoss_devel"
user = "geoss_devel"
password = "********"

[LF]
BASE_URL = 'https://geoss.devel.esaportal.eu/'
```

Create configuration file `environment_config.ini` for scripts that imports data.
Configure access to keycloak in the configuration file
```ini
[KC]
BASE_URL = 'https://GPP-IDP-DOMAIN'
USER_NAME = 'geoss'
USER_PASS = '****'
```
Configure API to geoss-contents in the configuration file
```ini
[contents]
DOCUMENT_API_URL = 'API_BASE_URL/contents/rest/document'
FOLDER_API_URL = 'API_BASE_URL/contents/rest/folder'
SITE_API_URL = 'API_BASE_URL/contents/rest/site'
```
Configure API to geoss-curated in the configuration file
```ini
[curated]
BOOKMARKED_API_URL = 'API_BASE_URL/curated/rest/bookmarked'
RATING_API_URL = 'API_BASE_URL/curated/rest/rating'
STATS_API_URL: 'API_BASE_URL/curated/rest/stats'
```
Configure API to geoss-personaldata in the configuration file
```ini
[personaldata]
SAVED_RUNS_API_URL = 'API_BASE_URL/personaldata/rest/saved-runs'
SAVED_SEARCHES_API_URL = 'API_BASE_URL/personaldata/rest/saved-searches'
SETTINGS_API_URL = 'API_BASE_URL/personaldata/rest/settings'
SURVEYS_API_URL = 'API_BASE_URL/personaldata/rest/surveys'
```
Configure API to geoss-settings in the configuration file
```ini
[setting]
LAYERS_API_URL = 'API_BASE_URL/settings/rest/layers'
TAGS_API_URL = 'API_BASE_URL/settings/rest/tags'
VIEWS_API_URL = 'API_BASE_URL/settings/rest/views'
```

For DEV, the example configuration is below:
```ini
[KC]
BASE_URL = 'https://gpp-idp.devel.esaportal.eu'
USER_NAME = 'geoss'
USER_PASS = '****'

[contents]
DOCUMENT_API_URL = 'https://gpp-admin.devel.esaportal.eu/contents/rest/document'
FOLDER_API_URL = 'https://gpp-admin.devel.esaportal.eu/contents/rest/folder'
SITE_API_URL = 'https://gpp-admin.devel.esaportal.eu/contents/rest/site'

[curated]
BOOKMARKED_API_URL = 'https://gpp-admin.devel.esaportal.eu/curated/rest/bookmarked'
RATING_API_URL = 'https://gpp-admin.devel.esaportal.eu/curated/rest/rating'
STATS_API_URL: 'https://gpp-admin.devel.esaportal.eu/curated/rest/stats'

[personaldata]
SAVED_RUNS_API_URL = 'https://gpp-admin.devel.esaportal.eu/personaldata/rest/saved-runs'
SAVED_SEARCHES_API_URL = 'https://gpp-admin.devel.esaportal.eu/personaldata/rest/saved-searches'
SETTINGS_API_URL = 'https://gpp-admin.devel.esaportal.eu/personaldata/rest/settings'
SURVEYS_API_URL = 'https://gpp-admin.devel.esaportal.eu/personaldata/rest/surveys'

[setting]
LAYERS_API_URL = 'https://gpp-admin.devel.esaportal.eu/settings/rest/layers'
TAGS_API_URL = 'https://gpp-admin.devel.esaportal.eu/settings/rest/tags'
VIEWS_API_URL = 'https://gpp-admin.devel.esaportal.eu/settings/rest/views'
```
