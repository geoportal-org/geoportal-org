#!/bin/sh

if [ -n "${MAINTENANCE_ON}" ]; then
  touch /var/www/html/maintenance/maintenance.on
else
  echo "MAINTENANCE_ON is not set skipping enable maintenance mode"
fi


if [ -n "${MAINTENANCE_WHITELIST}" ]; then

  maintenance_map=''

  for whitelist_it in $(echo ${MAINTENANCE_WHITELIST} | tr "," "\n")
  do
    maintenance_map="${maintenance_map}"'\n'"${whitelist_it}    1;"
  done

  mv /etc/nginx/conf.d/common/map_initial_setup.conf /etc/nginx/conf.d/common/map_initial_setup.conf.old
  awk -v r="${maintenance_map}" '{gsub(/###MAINTENANCE_WHITELIST_CONFIG###/,r)}1' /etc/nginx/conf.d/common/map_initial_setup.conf.old > /etc/nginx/conf.d/common/map_initial_setup.conf
  rm /etc/nginx/conf.d/common/map_initial_setup.conf.old
else
  echo "MAINTENANCE_WHITELIST is not set"
fi

