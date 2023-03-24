FROM node:18.15-slim as themes

WORKDIR /themes/geoss/account/src

COPY themes/geoss/account/src/ ./

RUN npm install
RUN npm run build
RUN npm run babel

FROM quay.io/keycloak/keycloak:21.0 as builder

# Enable health and metrics support
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

# Configure a database vendor
ARG KC_DB=mariadb
ENV KC_DB=${KC_DB}

WORKDIR /opt/keycloak

# for demonstration purposes only, please make sure to use proper certificates in production instead
RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -alias server -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore
RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:21.0 as runner

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-keycloak"

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en

COPY --from=builder /opt/keycloak/ /opt/keycloak/
COPY realms/ /opt/keycloak/data/import/
COPY themes/geoss/ /opt/keycloak/themes/geoss/
COPY --from=themes /themes/geoss/account/resources/ /opt/keycloak/themes/geoss/account/resources/
COPY certs/ /etc/x509/https/

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
CMD ["start", "--optimized", "--import-realm", "--spi-theme-welcome-theme=geoss"]
