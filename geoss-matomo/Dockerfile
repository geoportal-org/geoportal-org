FROM matomo:4.5.0-apache

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-matomo"

COPY common/plugins/ /opt/plugins
COPY apache2/sites-available/000-default.conf /etc/apache2/sites-available/000-default.conf
COPY entrypoint.sh /entrypoint.sh
RUN chmod 644 /etc/apache2/sites-available/000-default.conf
RUN chmod 755 /entrypoint.sh
