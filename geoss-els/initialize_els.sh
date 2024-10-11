#!/bin/bash
until curl --user elastic:$ELASTIC_PASSWORD -sS "http://localhost:9200/_cat/health?h=status" | grep -q "green\|yellow"; do
        sleep 1
done

echo "ENTRYPOINT: Creating indexes ..."
curl --user elastic:$ELASTIC_PASSWORD -XPUT 'http://localhost:9200/geoss-cr' -H 'Content-Type: application/json' -d @/indices/geoss-cr-index.json
curl --user elastic:$ELASTIC_PASSWORD -XPUT 'http://localhost:9200/geoss-cr-extensions' -H 'Content-Type: application/json' -d @/indices/geoss-cr-extensions-index.json
curl --user elastic:$ELASTIC_PASSWORD -XPUT 'http://localhost:9200/geoss-recommendation' -H 'Content-Type: application/json' -d @/indices/geoss-recommendation-index.json
echo "ENTRYPOINT: Indexes created."

echo "ENTRYPOINT: Setting password for kibana_system user ..."
elasticsearch-users useradd kibana_system -p $KIBANA_PASSWORD -r kibana_system || true
#curl --user elastic:$ELASTIC_PASSWORD -XPUT -H 'Content-Type: application/json' 'http://localhost:9200/_security/user/kibana_system/_password' -d '{ "password":"'"$KIBANA_PASSWORD"'" }'
echo "ENTRYPOINT: Password for kibana_system has been set."

echo "ENTRYPOINT: Creating kibana GEOSS ADMIN user ..."
curl --user elastic:$ELASTIC_PASSWORD -XPOST "localhost:9200/_security/user/geoss?pretty" -H 'Content-Type: application/json' -d'
{
  "password" : "'"$GEOSS_PASSWORD"'",
  "roles" : [ "superuser", "kibana_admin" ],
  "full_name" : "GEOSS ADMIN",
  "email" : null
}
'
echo "ENTRYPOINT: Kibana GEOSS ADMIN user created."
