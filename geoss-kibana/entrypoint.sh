#!/bin/bash

mv /opt/bitnami/kibana/config/kibana.yml /opt/bitnami/kibana/config/kibana.yml.old
awk -v r="${KIBANA_PASSWORD}" '{gsub(/###KIBANA_PASSWORD###/,r)}1' /opt/bitnami/kibana/config/kibana.yml.old > /opt/bitnami/kibana/config/kibana.yml
rm /opt/bitnami/kibana/config/kibana.yml.old

mv /opt/bitnami/kibana/config/kibana.yml /opt/bitnami/kibana/config/kibana.yml.old
awk -v r="${KIBANA_ELASTICSEARCH_URL}" '{gsub(/###KIBANA_ELASTICSEARCH_URL###/,r)}1' /opt/bitnami/kibana/config/kibana.yml.old > /opt/bitnami/kibana/config/kibana.yml
rm /opt/bitnami/kibana/config/kibana.yml.old

/create_index_patterns.sh&
/opt/bitnami/scripts/kibana/entrypoint.sh $1
