FROM debian:12.7 as builder

ENV NGINX_VERSION="1.27.1"

RUN apt-get update

RUN apt-get install -y lua5.3
RUN apt-get install -y build-essential bison ca-certificates curl dh-autoreconf doxygen flex gawk git iputils-ping libcurl4-gnutls-dev libexpat1-dev libgeoip-dev liblmdb-dev libpcre3-dev libssl-dev libtool libxml2 libxml2-dev libyajl-dev locales pkg-config wget zlib1g-dev libgd-dev
RUN rm -rf /var/lib/apt

# Compile modsecurity libraries
WORKDIR /opt
RUN git clone https://github.com/SpiderLabs/ModSecurity
WORKDIR /opt/ModSecurity
RUN git checkout v3/master && git submodule init && git submodule update
RUN ./build.sh
RUN ./configure
RUN make
RUN make install

# Fetch brothli compression modules
WORKDIR /opt
# Specify fixed dependency version for ngx_brotli repository
RUN git clone https://github.com/google/ngx_brotli
WORKDIR /opt/ngx_brotli
RUN git checkout 6e975bcb015f62e1f303054897783355e2a877dc && git submodule init && git submodule update


# Compile modsecurity nginx module
WORKDIR /opt
RUN git clone --depth 1 https://github.com/SpiderLabs/ModSecurity-nginx.git
RUN wget http://nginx.org/download/nginx-$NGINX_VERSION.tar.gz
RUN tar -xvzmf nginx-$NGINX_VERSION.tar.gz
WORKDIR /opt/nginx-$NGINX_VERSION
RUN ./configure --with-compat --add-dynamic-module=../ModSecurity-nginx --add-dynamic-module=../ngx_brotli
RUN make modules


FROM nginx:1.27.1 as runner

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-nginx"

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en

RUN apt-get update && apt-get install -y netcat-traditional logrotate
# Install modsecurity-helper libraries
RUN apt-get install -y libxml2 libxml2-dev libexpat1-dev libpcre3-dev libcurl4-gnutls-dev libyajl-dev liblmdb-dev libgeoip-dev dh-autoreconf
# Cleanup
RUN rm -rf /var/lib/apt

# Prepare
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

# Copy compiled modsecurity librarties
COPY --from=builder /usr/local/modsecurity /usr/local/modsecurity
# Copy compiled modsecurity module for nginx
COPY --from=builder /opt/nginx-$NGINX_VERSION/objs/ngx_http_modsecurity_module.so /usr/lib/nginx/modules
# Copy compiled brothli module for nginx
COPY --from=builder /opt/nginx-$NGINX_VERSION/objs/ngx_http_brotli_filter_module.so  /usr/lib/nginx/modules/
COPY --from=builder /opt/nginx-$NGINX_VERSION/objs/ngx_http_brotli_static_module.so  /usr/lib/nginx/modules/

# Setup noroot user 
RUN adduser -u 1500 --disabled-password --gecos "" docker_noroot
RUN chown -R docker_noroot:docker_noroot /var/cache/nginx \
 && chown -R docker_noroot:docker_noroot /var/nginx \
 && chown -R docker_noroot:docker_noroot /var/log/nginx \
 && chown -R docker_noroot:docker_noroot /etc/nginx 

RUN mkdir -p /var/run/ && touch /var/run/nginx.pid && \
    chown -R docker_noroot:docker_noroot /var/run/nginx.pid

#Allow run crond as non-root user
RUN chmod gu+rw /var/run
RUN chmod gu+s /usr/sbin/cron

USER docker_noroot
CMD ["nginx", "-g", "daemon off;"]
