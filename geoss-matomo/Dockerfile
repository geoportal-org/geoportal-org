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
