#!/bin/sh

if [ -z "$MARIADB_ROOT_PASSWORD" ]; then
    echo "$(date '+%Y-%m-%d %H:%M:%S') Missing MARIADB_ROOT_PASSWORD"
    exit 1
fi
if [ -z "$MARIADB_USER" ]; then
    echo "$(date '+%Y-%m-%d %H:%M:%S') Missing MARIADB_USER"
    exit 1
fi

if [ -z "MATOMO_DB_NAME" ] && [ -z "MATOMO_DB_USER_NAME" ] && [ -z "MATOMO_DB_USER_PASS" ]; then
    command="CREATE DATABASE IF NOT EXISTS $MATOMO_DB_NAME;"
    echo "$(date --rfc-3339=seconds) [Note] [Entrypoint]: $command"
    mysql -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" -e "$command"
    echo "$(date --rfc-3339=seconds) [Note] [Entrypoint]: Creating matomo user"
    command="CREATE USER '$MATOMO_DB_USER_NAME'@'localhost' IDENTIFIED BY '$MATOMO_DB_USER_PASS';"
    mysql -h localhost -u root -p"${MARIADB_ROOT_PASSWORD}" -e "$command"
    command="CREATE USER '$MATOMO_DB_USER_NAME'@'%' IDENTIFIED BY '$MATOMO_DB_USER_PASS';"
    mysql -h localhost -u root -p"${MARIADB_ROOT_PASSWORD}" -e "$command"
    command="GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, DROP, ALTER, CREATE TEMPORARY TABLES, LOCK TABLES ON $MATOMO_DB_NAME.* TO '$MATOMO_DB_USER_NAME'@'localhost';"
    mysql -h localhost -u root -p"${MARIADB_ROOT_PASSWORD}" -e "$command"
    command="GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, DROP, ALTER, CREATE TEMPORARY TABLES, LOCK TABLES ON $MATOMO_DB_NAME.* TO '$MATOMO_DB_USER_NAME'@'%';"
    mysql -h localhost -u root -p"${MARIADB_ROOT_PASSWORD}" -e "$command"
    echo "$(date --rfc-3339=seconds) [Note] [Entrypoint]: Finished initializing matomo database"
fi
