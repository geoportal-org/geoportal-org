#!/bin/bash

if [[ -z "$MARIADB_ROOT_PASSWORD" ]]; then
    echo "$(date '+%Y-%m-%d %H:%M:%S') Missing MARIADB_ROOT_PASSWORD"
    exit 1
fi
if [[ -z "$MARIADB_USER" ]]; then
    echo "$(date '+%Y-%m-%d %H:%M:%S') Missing MARIADB_USER"
    exit 1
fi

command="CREATE DATABASE IF NOT EXISTS geoss_bookmarks_db;"
echo "$(date '+%Y-%m-%d %H:%M:%S') $command"
mysql -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" -e "$command"
command="GRANT ALL ON geoss_bookmarks_db.* TO '$MARIADB_USER'@'%';"
echo "$(date '+%Y-%m-%d %H:%M:%S') $command"
mysql -h localhost -u root -p${MARIADB_ROOT_PASSWORD} -e "$command"

command="CREATE DATABASE IF NOT EXISTS geoss_contents_db;"
echo "$(date '+%Y-%m-%d %H:%M:%S') $command"
mysql -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" -e "$command"
command="GRANT ALL ON geoss_contents_db.* TO '$MARIADB_USER'@'%';"
echo "$(date '+%Y-%m-%d %H:%M:%S') $command"
mysql -h localhost -u root -p${MARIADB_ROOT_PASSWORD} -e "$command"

command="CREATE DATABASE IF NOT EXISTS geoss_keycloak_db;"
echo "$(date '+%Y-%m-%d %H:%M:%S') $command"
mysql -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" -e "$command"
command="GRANT ALL ON geoss_keycloak_db.* TO '$MARIADB_USER'@'%';"
echo "$(date '+%Y-%m-%d %H:%M:%S') $command"
mysql -h localhost -u root -p${MARIADB_ROOT_PASSWORD} -e "$command"

command="CREATE DATABASE IF NOT EXISTS geoss_matomo_db;"
echo "$(date '+%Y-%m-%d %H:%M:%S') $command"
mysql -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" -e "$command"
command="GRANT ALL ON geoss_matomo_db.* TO '$MARIADB_USER'@'%';"
echo "$(date '+%Y-%m-%d %H:%M:%S') $command"
mysql -h localhost -u root -p${MARIADB_ROOT_PASSWORD} -e "$command"

command="CREATE DATABASE IF NOT EXISTS geoss_settings_db;"
echo "$(date '+%Y-%m-%d %H:%M:%S') $command"
mysql -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" -e "$command"
command="GRANT ALL ON geoss_settings_db.* TO '$MARIADB_USER'@'%';"
echo "$(date '+%Y-%m-%d %H:%M:%S') $command"
mysql -h localhost -u root -p${MARIADB_ROOT_PASSWORD} -e "$command"

command="CREATE DATABASE IF NOT EXISTS geoss_personaldata_db;"
echo "$(date '+%Y-%m-%d %H:%M:%S') $command"
mysql -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" -e "$command"
command="GRANT ALL ON geoss_personaldata_db.* TO '$MARIADB_USER'@'%';"
echo "$(date '+%Y-%m-%d %H:%M:%S') $command"
mysql -h localhost -u root -p${MARIADB_ROOT_PASSWORD} -e "$command"
