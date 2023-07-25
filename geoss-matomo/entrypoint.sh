#!/bin/sh
set -e

# usage: file_env VAR [DEFAULT]
#    ie: file_env 'XYZ_DB_PASSWORD' 'example'
# (will allow for "$XYZ_DB_PASSWORD_FILE" to fill in the value of
#  "$XYZ_DB_PASSWORD" from a file, especially for Docker's secrets feature)
file_env() {
        local var="$1"
        local fileVar="${var}_FILE"
        local def="${2:-}"
        local varValue=$(env | grep -E "^${var}=" | sed -E -e "s/^${var}=//")
        local fileVarValue=$(env | grep -E "^${fileVar}=" | sed -E -e "s/^${fileVar}=//")
        if [ -n "${varValue}" ] && [ -n "${fileVarValue}" ]; then
                echo >&2 "error: both $var and $fileVar are set (but are exclusive)"
                exit 1
        fi
        if [ -n "${varValue}" ]; then
                export "$var"="${varValue}"
        elif [ -n "${fileVarValue}" ]; then
                export "$var"="$(cat "${fileVarValue}")"
        elif [ -n "${def}" ]; then
                export "$var"="$def"
        fi
        unset "$fileVar"
}

file_env 'MATOMO_DATABASE_HOST'
file_env 'MATOMO_DATABASE_USERNAME'
file_env 'MATOMO_DATABASE_PASSWORD'
file_env 'MATOMO_DATABASE_DBNAME'

if [ ! -e matomo.php ]; then
        tar cf - --one-file-system -C /usr/src/matomo . | tar xf -
        mv /opt/plugins/* /var/www/html/plugins/
        chown -R www-data:www-data .
fi

chown -R www-data:www-data /var/www/html

if [ -n "${MATOMO_DATABASE_HOST}" ]; then

  mv /tmp/config.ini.php.template /tmp/config.ini.php.template.old
  awk -v r="${MATOMO_DATABASE_HOST}" '{gsub(/###HOST###/,r)}1' /tmp/config.ini.php.template.old > /tmp/config.ini.php.template
  rm /tmp/config.ini.php.template.old
else
 echo "MATOMO_DATABASE_HOST env variable is not set"
fi

if [ -n "${MATOMO_DATABASE_USERNAME}" ]; then

  mv /tmp/config.ini.php.template /tmp/config.ini.php.template.old
  awk -v r="${MATOMO_DATABASE_USERNAME}" '{gsub(/###USERNAME###/,r)}1' /tmp/config.ini.php.template.old > /tmp/config.ini.php.template
  rm /tmp/config.ini.php.template.old
else
 echo "MATOMO_DATABASE_USERNAME env variable is not set"
fi

if [ -n "${MATOMO_DATABASE_PASSWORD}" ]; then

  mv /tmp/config.ini.php.template /tmp/config.ini.php.template.old
  awk -v r="${MATOMO_DATABASE_PASSWORD}" '{gsub(/###PASSWORD###/,r)}1' /tmp/config.ini.php.template.old > /tmp/config.ini.php.template
  rm /tmp/config.ini.php.template.old
else
 echo "MATOMO_DATABASE_PASSWORD env variable is not set"
fi

if [ -n "${MATOMO_DATABASE_DBNAME}" ]; then

  mv /tmp/config.ini.php.template /tmp/config.ini.php.template.old
  awk -v r="${MATOMO_DATABASE_DBNAME}" '{gsub(/###DBNAME###/,r)}1' /tmp/config.ini.php.template.old > /tmp/config.ini.php.template
  rm /tmp/config.ini.php.template.old
else
 echo "MATOMO_DATABASE_DBNAME env variable is not set"
fi

if [ -n "$MATOMO_DATABASE_HOST" ] && [ -n "$MATOMO_DATABASE_USERNAME" ] && [ -n "$MATOMO_DATABASE_PASSWORD" ] && [ -n "$MATOMO_DATABASE_DBNAME" ]; then
        chown 33:www-data /tmp/config.ini.php.template
        mv /tmp/config.ini.php.template /var/www/html/config/config.ini.php
        echo "Config.ini.php loaded"
else
        echo "Config.ini.php couldn't be loaded. One or more variables not set: MATOMO_DATABASE_HOST, MATOMO_DATABASE_USERNAME, MATOMO_DATABASE_PASSWORD, MATOMO_DATABASE_DBNAME."
fi

#TEMPORARY - workaround for huge node geoss-admin headers/cookies
if grep -Fq 'LimitRequestFieldSize' /etc/apache2/apache2.conf
then
        echo 'LimitRequestFieldSize config found';
else
        sed -i  '/^# Global configuration/a LimitRequestFieldSize 32768' /etc/apache2/apache2.conf
        echo 'LimitRequestFieldSize increased';
fi
exec "$@"
