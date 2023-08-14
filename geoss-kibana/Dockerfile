FROM bitnami/kibana:8.9.0

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-kibana"

COPY opt/bitnami/scripts/ /opt/bitnami/scripts

COPY --chown=1001 conf/kibana.yml /bitnami/kibana/conf/kibana.yml
COPY --chown=1001 ca/ /etc/kibana-data/

COPY index-patterns/ /etc/kibana-data/
COPY --chown=1001 entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
COPY --chown=1001 create_index_patterns.sh /create_index_patterns.sh
RUN chmod +x /create_index_patterns.sh

HEALTHCHECK \
	--interval=30s \
	--start-period=30s \
	--timeout=1m \
CMD curl -fsS "http://localhost:5601/" || exit 1

ENTRYPOINT ["/entrypoint.sh"]
CMD ["/opt/bitnami/scripts/kibana/run.sh"]