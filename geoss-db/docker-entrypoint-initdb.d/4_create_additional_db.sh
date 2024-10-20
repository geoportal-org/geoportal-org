#!/bin/sh

if [ -z "$MARIADB_ROOT_PASSWORD" ]; then
    echo "$(date '+%Y-%m-%d %H:%M:%S') Missing MARIADB_ROOT_PASSWORD"
    exit 1
fi
if [ -z "$MARIADB_USER" ]; then
    echo "$(date '+%Y-%m-%d %H:%M:%S') Missing MARIADB_USER"
    exit 1
fi

create_database()
{
    database=$1
    command="CREATE DATABASE IF NOT EXISTS $database;"
    echo "$(date --rfc-3339=seconds) [Note] [Entrypoint]: $command"
    mariadb -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" -e "$command"
    command="GRANT ALL ON $database.* TO '$MARIADB_USER'@'%';"
    echo "$(date --rfc-3339=seconds) [Note] [Entrypoint]: $command"
    mariadb -h localhost -u root -p"${MARIADB_ROOT_PASSWORD}" -e "$command"
}

create_database geoss_bookmarks_db
create_database geoss_contents_db
create_database geoss_keycloak_db
create_database geoss_settings_db
create_database geoss_personaldata_db
create_database geoss_curated_db
