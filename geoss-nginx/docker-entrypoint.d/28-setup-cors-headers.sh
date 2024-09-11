#!/bin/bash

allowed_domains_string=""

allowed_domains=()

if [ -n "${UI_DOMAIN_NAME}" ]; then
  allowed_domains+=("https://${UI_DOMAIN_NAME}")
fi

if [ -n "${ADMIN_DOMAIN_NAME}" ]; then
  allowed_domains+=("https://${ADMIN_DOMAIN_NAME}")
fi

if [ -n "${KEYCLOAK_DOMAIN_NAME}" ]; then
  allowed_domains+=("https://${KEYCLOAK_DOMAIN_NAME}")
fi

if [ -n "${LANDING_PAGE_DOMAIN_NAME}" ]; then
  allowed_domains+=("https://${LANDING_PAGE_DOMAIN_NAME}")
fi

for ((i = 0; i < ${#allowed_domains[@]}; i++)); do
    if [ $i -gt 0 ]; then
        allowed_domains_string="${allowed_domains_string}|${allowed_domains[i]}"
      else
        allowed_domains_string="${allowed_domains[i]}"
    fi
done

if [ -n "${allowed_domains_string}" ]; then
  allowed_domains_config='if ($http_origin ~* ('
  allowed_domains_config+=$allowed_domains_string
  allowed_domains_config+=$(cat <<'EOL'
)) {
    set $allowed_origin $http_origin;
}
EOL
  )

  mv /etc/nginx/conf.d/utils/proxy_response_headers.conf /etc/nginx/conf.d/utils/proxy_response_headers.conf.old
  awk -v r="${allowed_domains_config}" '{gsub(/###ALLOWED_DOMAINS_CONFIG###/,r)}1' /etc/nginx/conf.d/utils/proxy_response_headers.conf.old > /etc/nginx/conf.d/utils/proxy_response_headers.conf
  rm /etc/nginx/conf.d/utils/proxy_response_headers.conf.old

  mv /etc/nginx/conf.d/utils/proxy_response_headers_keycloak.conf /etc/nginx/conf.d/utils/proxy_response_headers_keycloak.conf.old
  awk -v r="${allowed_domains_config}" '{gsub(/###ALLOWED_DOMAINS_CONFIG###/,r)}1' /etc/nginx/conf.d/utils/proxy_response_headers_keycloak.conf.old > /etc/nginx/conf.d/utils/proxy_response_headers_keycloak.conf
  rm /etc/nginx/conf.d/utils/proxy_response_headers_keycloak.conf.old
fi