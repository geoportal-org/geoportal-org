FROM haproxy:latest

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-haproxy"

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en

COPY config/haproxy.cfg /usr/local/etc/haproxy/haproxy.cfg

EXPOSE 8000
EXPOSE 8080
EXPOSE 8081
EXPOSE 8082
