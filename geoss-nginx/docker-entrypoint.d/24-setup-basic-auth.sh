#!/bin/sh

# if [ "${DEPLOY_ENV}" = "uat" ]; then
#   include_conf='include conf.d/common/block_unauthorized_users.conf;'

#   mv /etc/nginx/conf.d/portal.conf /etc/nginx/conf.d/portal.conf.old
#   awk -v r="${include_conf}" '{gsub(/###BASIC_AUTH###/,r)}1' /etc/nginx/conf.d/portal.conf.old > /etc/nginx/conf.d/portal.conf
#   rm /etc/nginx/conf.d/portal.conf.old

#   mv /etc/nginx/conf.d/cms.conf /etc/nginx/conf.d/cms.conf.old
#   awk -v r="${include_conf}" '{gsub(/###BASIC_AUTH###/,r)}1' /etc/nginx/conf.d/cms.conf.old > /etc/nginx/conf.d/cms.conf
#   rm /etc/nginx/conf.d/cms.conf.old
# fi

if [ "${DEPLOY_ENV}" = "prod" ]; then
  include_conf='include conf.d/common/block_unauthorized_users_cms.conf;'

  mv /etc/nginx/conf.d/cms.conf /etc/nginx/conf.d/cms.conf.old
  awk -v r="${include_conf}" '{gsub(/###BASIC_AUTH###/,r)}1' /etc/nginx/conf.d/cms.conf.old > /etc/nginx/conf.d/cms.conf
  rm /etc/nginx/conf.d/cms.conf.old
fi

