#!/bin/bash

numoftables=0
if [ -n "$MATOMO_DATABASE_DBNAME" ] && [ -n "$MATOMO_DATABASE_USERNAME" ] && [ -n "$MATOMO_DATABASE_PASSWORD" ]; then
    while [ "$numoftables" -lt 30 ]
    do
        sleep 60
        numoftables=`mysql -s -N -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" -e "select count(table_name) from information_schema.tables where table_schema='matomodb';"`
    done;
fi;

mysql -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" $MATOMO_DATABASE_DBNAME -e "source /opt/force_matomo_anonymization.sql"
mysql -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" $MATOMO_DATABASE_DBNAME -e "update matomo_option set option_value=1 where option_name='PrivacyManager.ipAnonymizerEnabled';"
echo "Created matomo triggers - exiting"