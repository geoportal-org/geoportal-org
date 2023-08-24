#!/bin/bash
until curl --user elastic:$ELASTIC_PASSWORD -sS "http://localhost:9200/_cat/health?h=status" | grep -q "green\|yellow"; do
        sleep 1
done

echo "ENTRYPOINT: Creating indexes ..."
curl --user elastic:$ELASTIC_PASSWORD -XPUT 'http://localhost:9200/geoss-cr' -H 'Content-Type: application/json' -d @/indices/geoss-cr-index.json
curl --user elastic:$ELASTIC_PASSWORD -XPUT 'http://localhost:9200/geoss-cr-extensions' -H 'Content-Type: application/json' -d @/indices/geoss-cr-extensions-index.json
curl --user elastic:$ELASTIC_PASSWORD -XPUT 'http://localhost:9200/geoss-recommendation' -H 'Content-Type: application/json' -d @/indices/geoss-recommendation-index.json
curl --user elastic:$ELASTIC_PASSWORD -XPUT 'http://localhost:9200/thesaurus-vocabulary' -H 'Content-Type: application/json' -d @/indices/thesaurus-vocabulary-index.json
echo "ENTRYPOINT: Indexes created."

echo "ENTRYPOINT: Setting password for kibana_system user ..."
curl --user elastic:$ELASTIC_PASSWORD -XPUT -H 'Content-Type: application/json' 'http://localhost:9200/_security/user/kibana_system/_password' -d '{ "password":"test123" }'
echo "ENTRYPOINT: Password for kibana_system has been set."

echo "ENTRYPOINT: Creating kibana GEOSS ADMIN user ..."
curl --user elastic:$ELASTIC_PASSWORD -XPOST "localhost:9200/_security/user/geoss?pretty" -H 'Content-Type: application/json' -d'
{
  "password" : "test123",
  "roles" : [ "admin", "kibana_admin" ],
  "full_name" : "GEOSS ADMIN",
  "email" : "geoss@eversis.com"
}
'
echo "ENTRYPOINT: Kibana GEOSS ADMIN user created."