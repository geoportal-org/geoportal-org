#!/bin/sh

if [ "${DEPLOY_ENV}" = "uat" ]; then
  robots_conf='location = /robots.txt {
    add_header  Content-Type  text/plain;
    return 200 "User-agent: *\\nDisallow: /\\n";
  }'
  mv /etc/nginx/conf.d/cms.conf /etc/nginx/conf.d/cms.conf.old
  awk -v r="${robots_conf}" '{gsub(/###ROBOTS_CONFIG###/,r)}1' /etc/nginx/conf.d/cms.conf.old > /etc/nginx/conf.d/cms.conf
  rm /etc/nginx/conf.d/cms.conf.old
else
  echo "Skipping robots.txt configuration"
fi