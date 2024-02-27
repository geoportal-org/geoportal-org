if [ -n "${UI_SERVICES_PROVIDERS}" ]; then

  mv /etc/nginx/conf.d/gpp.conf /etc/nginx/conf.d/gpp.conf.old
  awk -v r="" '{gsub(/#SP#/,r)}1' /etc/nginx/conf.d/gpp.conf.old > /etc/nginx/conf.d/gpp.conf
  rm /etc/nginx/conf.d/gpp.conf.old

  mv /etc/nginx/conf.d/gpp.conf /etc/nginx/conf.d/gpp.conf.old
  awk -v r="${UI_SERVICES_PROVIDERS}" '{gsub(/###SERVICES_PROVIDERS_SOURCE###/,r)}1' /etc/nginx/conf.d/gpp.conf.old > /etc/nginx/conf.d/gpp.conf
  rm /etc/nginx/conf.d/gpp.conf.old
else
  echo "UI_SERVICES_PROVIDERS env variable is not set"
fi