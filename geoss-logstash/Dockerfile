FROM docker.elastic.co/logstash/logstash:8.9.0

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-logstash"

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en

WORKDIR /usr/share/logstash

RUN rm /usr/share/logstash/config/logstash-sample.conf
RUN rm /usr/share/logstash/pipeline/logstash.conf

COPY config/ /usr/share/logstash/config/
COPY lib/ /usr/share/logstash/lib/
COPY pipeline/ /usr/share/logstash/pipeline/
#COPY pipeline/logstash_geoss_cr.conf /usr/share/logstash/pipeline/
#COPY pipeline/logstash_geoss_cr_extensions.conf /usr/share/logstash/pipeline/
#COPY pipeline/logstash_recommendation.conf /usr/share/logstash/pipeline/
