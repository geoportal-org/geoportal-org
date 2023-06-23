#!/bin/sh

if [ -z "$MARIADB_ROOT_PASSWORD" ]; then
    echo "$(date '+%Y-%m-%d %H:%M:%S') Missing MARIADB_ROOT_PASSWORD"
    exit 1
fi
if [ -z "$MARIADB_USER" ]; then
    echo "$(date '+%Y-%m-%d %H:%M:%S') Missing MARIADB_USER"
    exit 1
fi

if [ -n "MATOMO_DATABASE_DBNAME" ] && [ -n "MATOMO_DATABASE_USERNAME" ] && [ -n "MATOMO_DATABASE_PASSWORD" ]; then
    command="CREATE DATABASE IF NOT EXISTS $MATOMO_DATABASE_DBNAME;"
    echo "$(date --rfc-3339=seconds) [Note] [Entrypoint]: $command"
    mysql -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" -e "$command"
    echo "$(date --rfc-3339=seconds) [Note] [Entrypoint]: Creating matomo user"
    command="CREATE USER '$MATOMO_DATABASE_USERNAME'@'localhost' IDENTIFIED BY '$MATOMO_DATABASE_PASSWORD';"
    mysql -h localhost -u root -p"${MARIADB_ROOT_PASSWORD}" -e "$command"
    command="CREATE USER '$MATOMO_DATABASE_USERNAME'@'%' IDENTIFIED BY '$MATOMO_DATABASE_PASSWORD';"
    mysql -h localhost -u root -p"${MARIADB_ROOT_PASSWORD}" -e "$command"
    command="GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, DROP, ALTER, CREATE TEMPORARY TABLES, LOCK TABLES ON $MATOMO_DATABASE_DBNAME.* TO '$MATOMO_DATABASE_USERNAME'@'localhost';"
    mysql -h localhost -u root -p"${MARIADB_ROOT_PASSWORD}" -e "$command"
    command="GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, DROP, ALTER, CREATE TEMPORARY TABLES, LOCK TABLES ON $MATOMO_DATABASE_DBNAME.* TO '$MATOMO_DATABASE_USERNAME'@'%';"
    mysql -h localhost -u root -p"${MARIADB_ROOT_PASSWORD}" -e "$command"
    command="GRANT FILE ON *.* TO '$MATOMO_DATABASE_USERNAME'@'localhost';"
    mysql -h localhost -u root -p"${MARIADB_ROOT_PASSWORD}" -e "$command"
    echo "$(date --rfc-3339=seconds) [Note] [Entrypoint]: Finished initializing matomo database"
fi
