FROM mariadb:11.4

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-db"

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en
ENV LC_ALL=en_GB.UTF-8

RUN apt-get update && \
    apt-get install -y locales && \
    locale-gen en_GB.UTF-8 && \
    update-locale LANG=en_GB.UTF-8

RUN apt-get update && \
    apt-get install -y apt-utils && \
    apt-get install -y unzip wget && \
    apt-get install -y python3 && \
    apt-get install -y python3-pip python3-dev build-essential && \
    pip install --break-system-packages awscli && \
    rm -rf /var/lib/apt

COPY config/my.cnf /etc/my.cnf
RUN chmod 644 /etc/my.cnf

COPY docker-entrypoint-initdb.d/1_try_restore_db_from_remote_url.sh /docker-entrypoint-initdb.d/1_try_restore_db_from_remote_url.sh
RUN chmod +x /docker-entrypoint-initdb.d/1_try_restore_db_from_remote_url.sh

COPY docker-entrypoint-initdb.d/2_try_restore_db_from_aws_bucket.sh /docker-entrypoint-initdb.d/2_try_restore_db_from_aws_bucket.sh
RUN chmod +x /docker-entrypoint-initdb.d/2_try_restore_db_from_aws_bucket.sh

COPY docker-entrypoint-initdb.d/3_try_restore_db_from_remote_db.sh /docker-entrypoint-initdb.d/3_try_restore_db_from_remote_db.sh
RUN chmod +x /docker-entrypoint-initdb.d/3_try_restore_db_from_remote_db.sh

COPY docker-entrypoint-initdb.d/4_create_additional_db.sh /docker-entrypoint-initdb.d/4_create_additional_db.sh
RUN chmod +x /docker-entrypoint-initdb.d/4_create_additional_db.sh

COPY docker-entrypoint-initdb.d/5_create_matomo_db.sh /docker-entrypoint-initdb.d/5_create_matomo_db.sh
RUN chmod +x /docker-entrypoint-initdb.d/5_create_matomo_db.sh

#MATOMO ANONYMIZATION IP ENFORCEMENT

COPY enableMatomoAnonymization.sh /opt/enableMatomoAnonymization.sh
RUN chmod +x /opt/enableMatomoAnonymization.sh

USER mysql

EXPOSE 3306
