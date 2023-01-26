#!/bin/bash

if [[ -z "$MARIADB_ROOT_PASSWORD" ]]; then
    echo "$(date '+%Y-%m-%d %H:%M:%S') Missing MARIADB_ROOT_PASSWORD"
    exit 1
fi
if [[ -z "$MARIADB_USER" ]]; then
    echo "$(date '+%Y-%m-%d %H:%M:%S') Missing MARIADB_USER"
    exit 1
fi

mysql -h localhost -u root -p${MARIADB_ROOT_PASSWORD} -e "create database if not exists geoss_keycloak_db;"
dbUserGrants="GRANT ALL ON geoss_keycloak_db.* TO '$MARIADB_USER'@'%';"
mysql -h localhost -u root -p${MARIADB_ROOT_PASSWORD} -e "${dbUserGrants}"

mysql -h localhost -u root -p${MARIADB_ROOT_PASSWORD} -e "create database if not exists geoss_bookmarks_db;"
dbUserGrants="GRANT ALL ON geoss_bookmarks_db.* TO '$MARIADB_USER'@'%';"
mysql -h localhost -u root -p${MARIADB_ROOT_PASSWORD} -e "${dbUserGrants}"

mysql -h localhost -u root -p${MARIADB_ROOT_PASSWORD} -e "create database if not exists geoss_contents_db;"
dbUserGrants="GRANT ALL ON geoss_contents_db.* TO '$MARIADB_USER'@'%';"
mysql -h localhost -u root -p${MARIADB_ROOT_PASSWORD} -e "${dbUserGrants}"

mysql -h localhost -u root -p${MARIADB_ROOT_PASSWORD} -e "create database if not exists geoss_settings_db;"
dbUserGrants="GRANT ALL ON geoss_settings_db.* TO '$MARIADB_USER'@'%';"
mysql -h localhost -u root -p${MARIADB_ROOT_PASSWORD} -e "${dbUserGrants}"

mysql -h localhost -u root -p${MARIADB_ROOT_PASSWORD} -e "create database if not exists geoss_userdata_db;"
dbUserGrants="GRANT ALL ON geoss_userdata_db.* TO '$MARIADB_USER'@'%';"
mysql -h localhost -u root -p${MARIADB_ROOT_PASSWORD} -e "${dbUserGrants}"
