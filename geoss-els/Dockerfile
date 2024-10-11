FROM docker.elastic.co/elasticsearch/elasticsearch:8.9.0

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-els"

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en

WORKDIR /usr/share/elasticsearch

RUN bin/elasticsearch-plugin install -b analysis-icu
RUN bin/elasticsearch-plugin install -b analysis-kuromoji
RUN bin/elasticsearch-plugin install -b analysis-smartcn
RUN bin/elasticsearch-plugin install -b analysis-stempel
RUN bin/elasticsearch-plugin install -b repository-azure

COPY indices/ /indices/
COPY --chown=elasticsearch  entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
COPY --chown=elasticsearch  initialize_els.sh /initialize_els.sh
RUN chmod +x /initialize_els.sh

ENTRYPOINT ["/entrypoint.sh"]
CMD ["eswrapper"]
