FROM bitnami/matomo:5.1.1

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-matomo"

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en

COPY --chown=1001 plugins/ /opt/bitnami/matomo/plugins/
COPY --chown=1001 conf/httpd-default.conf /opt/bitnami/apache/conf/extra/httpd-default.conf

USER root
RUN chown 1001 -R /opt/bitnami/apache/
RUN chown 1001 -R /opt/bitnami/
RUN chown 1001 -R /bitnami
RUN chown 1001 /opt/bitnami/php/etc/php.ini
USER 1001

ENV APACHE_HTTP_PORT_NUMBER=8080
ENV APACHE_HTTPS_PORT_NUMBER=8443

EXPOSE 8080
