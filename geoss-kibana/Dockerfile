FROM docker.elastic.co/kibana/kibana:8.7.0

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-kibana"

HEALTHCHECK \
	--interval=30s \
	--start-period=30s \
	--timeout=1m \
CMD curl -fsS "http://localhost:5601/" || exit 1
