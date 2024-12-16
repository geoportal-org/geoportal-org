FROM nginx:1.27.3 as runner

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-nginx"

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en

RUN apt-get update && apt-get upgrade -y && apt-get install -y logrotate nano
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
