#!/bin/sh

# if [ "${DEPLOY_ENV}" = "uat" ]; then
#   include_conf='include conf.d/common/block_unauthorized_users.conf;'

#    mv /etc/nginx/conf.d/gpp.conf /etc/nginx/conf.d/gpp.conf.old
#    awk -v r="${include_conf}" '{gsub(/###BASIC_AUTH###/,r)}1' /etc/nginx/conf.d/gpp.conf.old > /etc/nginx/conf.d/gpp.conf
#    rm /etc/nginx/conf.d/gpp.conf.old
#
#    mv /etc/nginx/conf.d/gpp-idp.conf /etc/nginx/conf.d/gpp-idp.conf.old
#    awk -v r="${include_conf}" '{gsub(/###BASIC_AUTH###/,r)}1' /etc/nginx/conf.d/gpp-idp.conf.old > /etc/nginx/conf.d/gpp-idp.conf
#    rm /etc/nginx/conf.d/gpp-idp.conf.old
#
#    mv /etc/nginx/conf.d/gpp-admin.conf /etc/nginx/conf.d/gpp-admin.conf.old
#    awk -v r="${include_conf}" '{gsub(/###BASIC_AUTH###/,r)}1' /etc/nginx/conf.d/gpp-admin.conf.old > /etc/nginx/conf.d/gpp-admin.conf
#    rm /etc/nginx/conf.d/gpp-admin.conf.old
# fi

if [ "${DEPLOY_ENV}" = "prod" ]; then
  include_conf='include conf.d/common/block_unauthorized_users_ext.conf;'

  mv /etc/nginx/conf.d/gpp.conf /etc/nginx/conf.d/gpp.conf.old
  awk -v r="${include_conf}" '{gsub(/###BASIC_AUTH###/,r)}1' /etc/nginx/conf.d/gpp.conf.old > /etc/nginx/conf.d/gpp.conf
  rm /etc/nginx/conf.d/gpp.conf.old

  mv /etc/nginx/conf.d/gpp-idp.conf /etc/nginx/conf.d/gpp-idp.conf.old
  awk -v r="${include_conf}" '{gsub(/###BASIC_AUTH###/,r)}1' /etc/nginx/conf.d/gpp-idp.conf.old > /etc/nginx/conf.d/gpp-idp.conf
  rm /etc/nginx/conf.d/gpp-idp.conf.old

  mv /etc/nginx/conf.d/gpp-admin.conf /etc/nginx/conf.d/gpp-admin.conf.old
  awk -v r="${include_conf}" '{gsub(/###BASIC_AUTH###/,r)}1' /etc/nginx/conf.d/gpp-admin.conf.old > /etc/nginx/conf.d/gpp-admin.conf
  rm /etc/nginx/conf.d/gpp-admin.conf.old
fi

