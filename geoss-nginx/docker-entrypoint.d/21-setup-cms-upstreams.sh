#!/bin/sh

if [ -n "${CMS_UPSTREAM_HOST}" ] && [ -n "${CMS_UPSTREAM_PORT}" ]; then

  i=0
  upstream_conf=''
  upstream_port=${CMS_UPSTREAM_PORT}

  kibana_upstream_conf=''

  for upstream_host in $(echo ${CMS_UPSTREAM_HOST} | tr "," "\n")
  do
  	i=$((i+1))
  	if [ $i -gt 1 ]; then
    		upstream_conf="${upstream_conf}"'\n'"server   ${upstream_host}:${upstream_port}  backup max_fails=1 fail_timeout=30;"
    	else
    		upstream_conf="${upstream_conf}"'\n'"server   ${upstream_host}:${upstream_port}  max_fails=2 fail_timeout=5;"
    		#kibana_upstream_conf="server   ${upstream_host}:5601;"
  	fi
  done

  mv /etc/nginx/conf.d/cms.conf /etc/nginx/conf.d/cms.conf.old
  awk -v r="${upstream_conf}" '{gsub(/###UPSTREAM_CONFIG###/,r)}1' /etc/nginx/conf.d/cms.conf.old > /etc/nginx/conf.d/cms.conf
  rm /etc/nginx/conf.d/cms.conf.old

  # mv /etc/nginx/conf.d/cms.conf /etc/nginx/conf.d/cms.conf.old
  # awk -v r="${kibana_upstream_conf}" '{gsub(/###KIBANA_UPSTREAM_CONFIG###/,r)}1' /etc/nginx/conf.d/cms.conf.old > /etc/nginx/conf.d/cms.conf
  # rm /etc/nginx/conf.d/cms.conf.old
else
  echo "CMS_UPSTREAM_HOST or CMS_UPSTREAM_PORT env variable is not set"
fi

#  upstream_conf="server   ${MONITORING_UPSTREAM_HOST}:80;"

#  mv /etc/nginx/conf.d/cms.conf /etc/nginx/conf.d/cms.conf.old
#  awk -v r="${upstream_conf}" '{gsub(/###MONITORING_UPSTREAM_CONFIG###/,r)}1' /etc/nginx/conf.d/cms.conf.old > /etc/nginx/conf.d/cms.conf
#  rm /etc/nginx/conf.d/cms.conf.old
# else
#  echo "MONITORING_UPSTREAM_HOST env variable is not set"
# fi

if [ -n "${KEYCLOAK_UPSTREAM_HOST}" ]; then

  i=0
  upstream_conf=""

  for upstream_host in $(echo ${KEYCLOAK_UPSTREAM_HOST} | tr "," "\n")
    do
      i=$((i+1))
      if [ $i -gt 1 ]; then
          upstream_conf="${upstream_conf}"'\n'"server   ${upstream_host}:8088  backup max_fails=1 fail_timeout=30;"
        else
          upstream_conf="${upstream_conf}"'\n'"server   ${upstream_host}:8088  max_fails=2 fail_timeout=5;"
      fi
  done

  mv /etc/nginx/conf.d/cms.conf /etc/nginx/conf.d/cms.conf.old
  awk -v r="${upstream_conf}" '{gsub(/###KEYCLOAK_UPSTREAM_CONFIG###/,r)}1' /etc/nginx/conf.d/cms.conf.old > /etc/nginx/conf.d/cms.conf
  rm /etc/nginx/conf.d/cms.conf.old
else
 echo "KEYCLOAK_UPSTREAM_HOST env variable is not set"
fi