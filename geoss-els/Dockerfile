FROM docker.elastic.co/elasticsearch/elasticsearch:8.7.0

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-els"

WORKDIR /usr/share/elasticsearch

RUN bin/elasticsearch-plugin install -b analysis-icu
RUN bin/elasticsearch-plugin install -b analysis-kuromoji
RUN bin/elasticsearch-plugin install -b analysis-smartcn
RUN bin/elasticsearch-plugin install -b analysis-stempel

COPY geoss-cr/* /tmp/
COPY geoss-cr-extensions/* /tmp/
COPY geoss-recommendation/* /tmp/
COPY --chown=elasticsearch entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

HEALTHCHECK \
	--interval=30s \
	--start-period=30s \
	--timeout=1m \
CMD curl -s http://localhost:9200/_cluster/health | grep -vq '"status":"red"' || exit 1

ENTRYPOINT ["/entrypoint.sh"]
CMD ['tail', '-f', '/dev/null']