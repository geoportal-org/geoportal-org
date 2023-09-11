 #!/bin/sh
 
 domains=""
 
 if [ -n "${CSP_DOMAINS}" ]; then
   for csp_domain in $(echo ${CSP_DOMAINS} | tr "," "\n")
   do
     domains="${domains} ${csp_domain}"
   done
 fi
 
 headers="add_header Content-Security-Policy \"default-src 'self' 'unsafe-inline' 'unsafe-eval' blob: data:${domains};\" always;"
 #headers="add_header Content-Security-Policy \"default-src 'self' https://gpp.devel.esaportal.eu/ https://gpp-idp.devel.esaportal.eu/; frame-ancestors 'self' https://gpp.devel.esaportal.eu/ https://gpp-idp.devel.esaportal.eu/; frame-src 'self' https://gpp.devel.esaportal.eu/ https://gpp-idp.devel.esaportal.eu/\" always;"
 
# mv /etc/nginx/conf.d/utils/proxy_response_headers.conf /etc/nginx/conf.d/utils/proxy_response_headers.conf.old
# awk -v r="${headers}" '{gsub(/###CSP_HEADERS###/,r)}1' /etc/nginx/conf.d/utils/proxy_response_headers.conf.old > /etc/nginx/conf.d/utils/proxy_response_headers.conf
# rm /etc/nginx/conf.d/utils/proxy_response_headers.conf.old

