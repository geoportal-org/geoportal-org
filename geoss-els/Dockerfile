FROM bitnami/elasticsearch:8.9.0

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-els"

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en

WORKDIR /opt/bitnami/elasticsearch

RUN bin/elasticsearch-plugin install -b analysis-icu
RUN bin/elasticsearch-plugin install -b analysis-kuromoji
RUN bin/elasticsearch-plugin install -b analysis-smartcn
RUN bin/elasticsearch-plugin install -b analysis-stempel

COPY --chown=1001 conf/elasticsearch.yml /opt/bitnami/elasticsearch/config/elasticsearch.yml

COPY indices/ /indices/
COPY --chown=1001 entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
COPY --chown=1001 create_indexes.sh /create_indexes.sh
RUN chmod +x /create_indexes.sh

HEALTHCHECK \
	--interval=30s \
	--start-period=30s \
	--timeout=1m \
CMD curl -s http://localhost:9200/_cluster/health | grep -vq '"status":"red"' || exit 1

ENTRYPOINT ["/entrypoint.sh"]
CMD ["/opt/bitnami/scripts/elasticsearch/run.sh"]