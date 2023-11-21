#!/bin/bash

numoftables=0

while [ "$numoftables" -lt 30 ]
do
    sleep 60
    numoftables=`mysql -s -N -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" -e "select count(table_name) from information_schema.tables where table_schema='matomodb';"`
done;


mysql -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" $MATOMO_DATABASE_DBNAME -e "update matomo_option set option_value=1 where option_name IN ('PrivacyManager.ipAnonymizerEnabled', 'PrivacyManager.useAnonymizedIpForVisitEnrichment');"
mysql -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" $MATOMO_DATABASE_DBNAME -e "update matomo_option set option_value=2 where option_name='PrivacyManager.ipAddressMaskLength';"
mysql -h localhost -u root -p"$MARIADB_ROOT_PASSWORD" $MATOMO_DATABASE_DBNAME -e "update matomo_option set option_value=0 where option_name IN ('PrivacyManager.anonymizeUserId', 'PrivacyManager.anonymizeOrderId', 'PrivacyManager.forceCookielessTracking');"

echo "Enabled matomo IP anonymization."