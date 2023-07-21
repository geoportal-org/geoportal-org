#!/bin/bash

echo "ENTRYPOINT: Starting ealsticsearch..."

until curl -sS "http://localhost:9200/_cat/health?h=status" | grep -q "green\|yellow"; do
        sleep 1
done

echo "ENTRYPOINT: Creating indexes ..."

head -n-1 /tmp/geoss-cr-settings.json > /tmp/geoss-cr.json; sed -i '$ s/$/%SEPARATOR%/' /tmp/geoss-cr.json; tail -n+2 /tmp/geoss-cr-mappings.json >> /tmp/geoss-cr.json; sed -i 's/%SEPARATOR%/,/' /tmp/geoss-cr.json
head -n-1 /tmp/geoss-cr-extensions-settings.json > /tmp/geoss-cr-extensions.json; sed -i '$ s/$/%SEPARATOR%/' /tmp/geoss-cr-extensions.json; tail -n+2 /tmp/geoss-cr-extensions-mappings.json >> /tmp/geoss-cr-extensions.json; sed -i 's/%SEPARATOR%/,/' /tmp/geoss-cr-extensions.json
head -n-1 /tmp/geoss-recommendation-settings.json > /tmp/geoss-recommendation.json; sed -i '$ s/$/%SEPARATOR%/' /tmp/geoss-recommendation.json; tail -n+2 /tmp/geoss-recommendation-mappings.json >> /tmp/geoss-recommendation.json; sed -i 's/%SEPARATOR%/,/' /tmp/geoss-recommendation.json

curl -XPUT 'http://localhost:9200/geoss-cr' -H "Content-Type: application/json" -d @/tmp/geoss-cr.json
curl -XPUT 'http://localhost:9200/geoss-cr-extensions' -H "Content-Type: application/json" -d @/tmp/geoss-cr-extensions.json
curl -XPUT 'http://localhost:9200/geoss-recommendation' -H "Content-Type: application/json" -d @/tmp/geoss-recommendation.json

echo "ENTRYPOINT: Indexes created"