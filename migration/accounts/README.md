# Migrate user accounts from Liferay 6.1 to Keycloak

## Prerequisites
- bash or compatible POSIX shell
- installed programs: curl, jq, mysql
- user with access to LF database
- user from keycloak in realm geoss assigned to group realm-manager

## Scripts

Common environment variables used by scripts:

| Environment variable | Description                                     |
|----------------------|-------------------------------------------------|
| LF_DB_HOST           | Liferay database host IP                        |
| LF_DB_PORT           | Liferay database port                           |
| LF_DB_NAME           | Liferay database name                           |
| LF_DB_USER           | Liferay database user name                      |
| LF_DB_PASS           | Liferay database user password                  |
| KC_BASE_URL          | Keycloak base URL                               |
| KC_USER_NAME         | Keycloak administrator username for realm geoss |
| KC_USER_PASS         | Keycloak administrator password for realm geoss |

These environment variables can be overwritten with script parameters.

### export_users_from_liferay
This script exports users from liferay database to cs file `liferay_users.csv`

e.g. with environments
```
export LF_DB_HOST=10.254.2.196
export LF_DB_PORT=3306
export LF_DB_NAME=geoss_devel
export LF_DB_USER=geoss_devel
export LF_DB_PASS=*******
./import_users_from_liferay.sh
```
e.g. with parameters
```
./export_users_from_liferay.sh --host=10.254.2.196 --port=3306 --database=geoss_devel --username=geoss_devel --password=********
```

### import_users_from_liferay
This script imports users from csv file `liferay_users.csv` to keycloak to geoss realm by Admin REST API

e.g. with environments
```
export KC_BASE_URL=https://gpp-idp.devel.esaportal.eu
export KC_USER_NAME=geoss
export KC_USER_PASS=*****
```
e.g. with parameters
```
./import_users_from_liferay.sh --baseurl=https://gpp-idp.devel.esaportal.eu --username=geoss --password=********
```

### get_access_token
This script login user to Keycloak and print access token to be used in other requests.

e.g. with environments
```
export KC_BASE_URL=https://gpp-idp.devel.esaportal.eu
export KC_USER_NAME=geoss
export KC_USER_PASS=*****
```
e.g. with parameters
```
./get_access_token.sh --baseurl=https://gpp-idp.devel.esaportal.eu --username=geoss --password=********
```

### get_user_id_map
This script print map with old liferay user id and new user id from keycloak.

e.g. with environments
```
export KC_BASE_URL=https://gpp-idp.devel.esaportal.eu
export KC_USER_NAME=geoss
export KC_USER_PASS=*****
```
e.g. with parameters
```
./get_user_id_map.sh --baseurl=https://gpp-idp.devel.esaportal.eu --username=geoss --password=********
```

### get_user_id_by_liferay_user_id
This script get keycloak user id by liferay user id. Liferay user id should be pass to script by argument.

e.g. with environments
```
export KC_BASE_URL=https://gpp-idp.devel.esaportal.eu
export KC_USER_NAME=geoss
export KC_USER_PASS=*****
./get_user_id_by_liferay_user_id.sh 1234
```
e.g. with parameters
```
./get_user_id_by_liferay_user_id.sh --baseurl=https://gpp-idp.devel.esaportal.eu --username=geoss --password=******** 1234
```
