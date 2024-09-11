#!/bin/sh

if [ -n "${UI_UPSTREAM_HOST}" ] && [ -n "${UI_UPSTREAM_PORT}" ]; then

  i=0
  upstream_conf=''
  upstream_port=${UI_UPSTREAM_PORT}

  kibana_upstream_conf=''

  for upstream_host in $(echo ${UI_UPSTREAM_HOST} | tr "," "\n")
  do
  	i=$((i+1))
  	if [ $i -gt 1 ]; then
    		upstream_conf="${upstream_conf}"'\n'"server   ${upstream_host}:${upstream_port}  backup max_fails=1 fail_timeout=30;"
    	else
    		upstream_conf="${upstream_conf}"'\n'"server   ${upstream_host}:${upstream_port}  max_fails=2 fail_timeout=5;"
    		#kibana_upstream_conf="server   ${upstream_host}:5601;"
  	fi
  done

  mv /etc/nginx/conf.d/gpp.conf /etc/nginx/conf.d/gpp.conf.old
  awk -v r="${upstream_conf}" '{gsub(/###UPSTREAM_CONFIG###/,r)}1' /etc/nginx/conf.d/gpp.conf.old > /etc/nginx/conf.d/gpp.conf
  rm /etc/nginx/conf.d/gpp.conf.old
else
  echo "UI_UPSTREAM_HOST or UI_UPSTREAM_PORT env variable is not set"
fi

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

  mv /etc/nginx/conf.d/gpp-idp.conf /etc/nginx/conf.d/gpp-idp.conf.old
  awk -v r="${upstream_conf}" '{gsub(/###KEYCLOAK_UPSTREAM_CONFIG###/,r)}1' /etc/nginx/conf.d/gpp-idp.conf.old > /etc/nginx/conf.d/gpp-idp.conf
  rm /etc/nginx/conf.d/gpp-idp.conf.old
else
 echo "KEYCLOAK_UPSTREAM_HOST env variable is not set"
fi


if [ -n "${ADMIN_UPSTREAM_HOST}" ]; then

  i=0
  upstream_conf=""

  for upstream_host in $(echo ${ADMIN_UPSTREAM_HOST} | tr "," "\n")
    do
      i=$((i+1))
      if [ $i -gt 1 ]; then
          upstream_conf="${upstream_conf}"'\n'"server   ${upstream_host}:8089  backup max_fails=1 fail_timeout=30;"
        else
          upstream_conf="${upstream_conf}"'\n'"server   ${upstream_host}:8089  max_fails=2 fail_timeout=5;"
      fi
  done

  mv /etc/nginx/conf.d/gpp-admin.conf /etc/nginx/conf.d/gpp-admin.conf.old
  awk -v r="${upstream_conf}" '{gsub(/###ADMIN_UPSTREAM_CONFIG###/,r)}1' /etc/nginx/conf.d/gpp-admin.conf.old > /etc/nginx/conf.d/gpp-admin.conf
  rm /etc/nginx/conf.d/gpp-admin.conf.old
else
 echo "ADMIN_UPSTREAM_HOST env variable is not set"
fi

if [ -n "${LANDINGPAGE_UPSTREAM_HOST}" ]; then

  i=0
  upstream_conf=""

  for upstream_host in $(echo ${LANDINGPAGE_UPSTREAM_HOST} | tr "," "\n")
    do
      i=$((i+1))
      if [ $i -gt 1 ]; then
          upstream_conf="${upstream_conf}"'\n'"server   ${upstream_host}:3002  backup max_fails=1 fail_timeout=30;"
        else
          upstream_conf="${upstream_conf}"'\n'"server   ${upstream_host}:3002  max_fails=2 fail_timeout=5;"
      fi
  done

  mv /etc/nginx/conf.d/gpp-lp.conf /etc/nginx/conf.d/gpp-lp.conf.old
  awk -v r="${upstream_conf}" '{gsub(/###UPSTREAM_CONFIG###/,r)}1' /etc/nginx/conf.d/gpp-lp.conf.old > /etc/nginx/conf.d/gpp-lp.conf
  rm /etc/nginx/conf.d/gpp-lp.conf.old
else
 echo "LANDINGPAGE_UPSTREAM_HOST env variable is not set"
fi