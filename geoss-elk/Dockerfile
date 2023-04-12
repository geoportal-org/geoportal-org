FROM docker.elastic.co/elasticsearch/elasticsearch:8.7.0

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-elk"

WORKDIR /usr/share/elasticsearch

RUN bin/elasticsearch-plugin install -b analysis-icu
RUN bin/elasticsearch-plugin install -b analysis-kuromoji
RUN bin/elasticsearch-plugin install -b analysis-smartcn
RUN bin/elasticsearch-plugin install -b analysis-stempel

HEALTHCHECK \
	--interval=30s \
	--start-period=30s \
	--timeout=1m \
CMD curl -fsS "http://localhost:9200/" || exit 1
