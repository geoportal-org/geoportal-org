FROM bitnami/kibana:8.9.0

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-kibana"

COPY opt/bitnami/scripts/ /opt/bitnami/scripts

COPY index-patterns/ /etc/kibana-data/
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

HEALTHCHECK \
	--interval=30s \
	--start-period=30s \
	--timeout=1m \
CMD curl -fsS "http://localhost:5601/" || exit 1

ENTRYPOINT ["/entrypoint.sh"]