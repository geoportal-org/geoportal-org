#!/bin/bash

if [ -n "$MATOMO_DATABASE_DBNAME" ] && [ -n "$MATOMO_DATABASE_USERNAME" ] && [ -n "$MATOMO_DATABASE_PASSWORD" ]; then
    while [ "$numoftables" -lt 20 ]
    sleep 120
    numoftables = `mysql -s -N -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" -e "select count(table_name) from information_schema.tables where table_schema='matomodb';"`
    do
        tf=`mysql -s -N -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" -e "select count(trigger_name) from information_schema.triggers where trigger_schema='matomodb' and trigger_name like '%_matomo';"`
        if [ "$tf" -lt 2 ]; then
            mysql -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" $MATOMO_DATABASE_USERNAME < /opt/force_matomo_anonymization.sql
            break
    end if;
    done
end if;