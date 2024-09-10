#!/bin/sh

if [ "${DEPLOY_ENV}" = "uat" ]; then
  robots_conf='location = /robots.txt {
    add_header  Content-Type  text/plain;
    return 200 "User-agent: *\\nDisallow: /\\n";
  }'
  mv /etc/nginx/conf.d/gpp.conf /etc/nginx/conf.d/gpp.conf.old
  awk -v r="${robots_conf}" '{gsub(/###ROBOTS_CONFIG###/,r)}1' /etc/nginx/conf.d/gpp.conf.old > /etc/nginx/conf.d/gpp.conf
  rm /etc/nginx/conf.d/gpp.conf.old

  mv /etc/nginx/conf.d/gpp-idp.conf /etc/nginx/conf.d/gpp-idp.conf.old
  awk -v r="${robots_conf}" '{gsub(/###ROBOTS_CONFIG###/,r)}1' /etc/nginx/conf.d/gpp-idp.conf.old > /etc/nginx/conf.d/gpp-idp.conf
  rm /etc/nginx/conf.d/gpp-idp.conf.old

  mv /etc/nginx/conf.d/gpp-admin.conf /etc/nginx/conf.d/gpp-admin.conf.old
  awk -v r="${robots_conf}" '{gsub(/###ROBOTS_CONFIG###/,r)}1' /etc/nginx/conf.d/gpp-admin.conf.old > /etc/nginx/conf.d/gpp-admin.conf
  rm /etc/nginx/conf.d/gpp-admin.conf.old

  mv /etc/nginx/conf.d/gpp-lp.conf /etc/nginx/conf.d/gpp-lp.conf.old
  awk -v r="${robots_conf}" '{gsub(/###ROBOTS_CONFIG###/,r)}1' /etc/nginx/conf.d/gpp-lp.conf.old > /etc/nginx/conf.d/gpp-lp.conf
  rm /etc/nginx/conf.d/gpp-lp.conf.old
else
  echo "Skipping robots.txt configuration"
fi