FROM openjdk:21-jdk-slim AS java-builder

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en
ENV GRADLE_USER_HOME=/opt/app/.gradle

WORKDIR /opt/app

COPY ./ ./

RUN chmod u+x gradlew
RUN ./gradlew --build-cache --no-daemon assemble

RUN mkdir keycloak-providers
RUN find ./**/build/libs -type f -name '*.jar' -exec echo {} \; -exec cp {} keycloak-providers/ \;

FROM quay.io/keycloak/keycloak:26.0 as builder

# Enable health and metrics support
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

# Configure a database vendor
ARG KC_DB=mariadb
ENV KC_DB=${KC_DB}

WORKDIR /opt/keycloak

COPY --from=java-builder --chown=keycloak --chmod=644 /opt/app/keycloak-providers/ /opt/keycloak/providers/

# for demonstration purposes only, please make sure to use proper certificates in production instead
#RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -alias server -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore
RUN /opt/keycloak/bin/kc.sh build --features="token-exchange"

FROM quay.io/keycloak/keycloak:26.0 as runner

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-keycloak"

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en

COPY --from=builder /opt/keycloak/ /opt/keycloak/
COPY --chown=keycloak realms/ /opt/keycloak/data/import/
COPY --chown=keycloak themes/geoss/account/ /opt/keycloak/themes/geoss/account/
COPY --chown=keycloak themes/geoss/admin/ /opt/keycloak/themes/geoss/admin/
COPY --chown=keycloak themes/geoss/email/ /opt/keycloak/themes/geoss/email/
COPY --chown=keycloak themes/geoss/login/ /opt/keycloak/themes/geoss/login/
COPY --chown=keycloak themes/geoss/welcome/ /opt/keycloak/themes/geoss/welcome/
COPY --chown=keycloak utils/ /opt/utils/

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
CMD ["start", "--optimized", "--import-realm", "--spi-theme-welcome-theme=geoss", "--https-certificate-file=/etc/x509/https/tls.crt", "--https-certificate-key-file=/etc/x509/https/tls.key"]
