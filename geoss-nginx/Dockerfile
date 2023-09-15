FROM nexus-docker.eversis.com:7443/common/base_nginx:1.23.3

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-nginx"

ENV no_proxy="127.0.0.1,localhost"
ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en

# Prepare directories
RUN rm /etc/nginx/conf.d/default.conf \
    && mkdir /var/nginx 
# \
#    && mkdir -p /var/nginx/cache/dynamic/admin_geoss \
#    && mkdir -p /var/nginx/cache/static/admin_geoss \
#    && mkdir -p /var/nginx/cache/dynamic/geoss \
#    && mkdir -p /var/nginx/cache/static/geoss

# Copy configuration files
COPY docker-entrypoint.d/ /docker-entrypoint.d
COPY etc/nginx/ /etc/nginx
COPY etc/logrotate.d/ /etc/logrotate.d
COPY var/www/html/ /var/www/html


RUN chmod +x /docker-entrypoint.d/*.sh
RUN chmod 644 /etc/logrotate.d/nginx
RUN chmod 664 /var/log/nginx
# RUN mkdir -p /nfs/geoss


CMD ["nginx", "-g", "daemon off;"]
