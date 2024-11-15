if [ "${BLOCK_ALL_FOR_SCAN}" = "true" ]; then
  mv /etc/nginx/conf.d/common/block_unauthorized_users_ext.conf /etc/nginx/conf.d/common/block_unauthorized_users_ext.conf.old
  awk -v r="#auth_basic" '{gsub(/auth_basic/,r)}1' /etc/nginx/conf.d/common/block_unauthorized_users_ext.conf.old > /etc/nginx/conf.d/common/block_unauthorized_users_ext.conf
  rm /etc/nginx/conf.d/common/block_unauthorized_users_ext.conf.old
fi

if [ "${BASIC_AUTH_ENABLED}" = "true" ]; then
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

  mv /etc/nginx/conf.d/gpp-lp.conf /etc/nginx/conf.d/gpp-lp.conf.old
  awk -v r="${include_conf}" '{gsub(/###BASIC_AUTH###/,r)}1' /etc/nginx/conf.d/gpp-lp.conf.old > /etc/nginx/conf.d/gpp-lp.conf
  rm /etc/nginx/conf.d/gpp-lp.conf.old

  if [ "${UI_REDIRECT_ENABLED}" = "true" ]; then
    mv /etc/nginx/conf.d/gpp-redirect.conf /etc/nginx/conf.d/gpp-redirect.conf.old
    awk -v r="${include_conf}" '{gsub(/###BASIC_AUTH###/,r)}1' /etc/nginx/conf.d/gpp-redirect.conf.old > /etc/nginx/conf.d/gpp-redirect.conf
    rm /etc/nginx/conf.d/gpp-redirect.conf.old
  fi 
fi

if [ -n "${BASIC_AUTH_WHITELIST}" ]; then
  whitelist_map=''

  for whitelist_it in $(echo ${BASIC_AUTH_WHITELIST} | tr "," "\n")
  do
    whitelist_map="${whitelist_map}"'\n'"allow ${whitelist_it};"
  done

  mv /etc/nginx/conf.d/common/block_unauthorized_users_ext.conf /etc/nginx/conf.d/common/block_unauthorized_users_ext.conf.old
  awk -v r="${whitelist_map}" '{gsub(/###BASIC_AUTH_WHITELIST###/,r)}1' /etc/nginx/conf.d/common/block_unauthorized_users_ext.conf.old > /etc/nginx/conf.d/common/block_unauthorized_users_ext.conf
  rm /etc/nginx/conf.d/common/block_unauthorized_users_ext.conf.old
fi