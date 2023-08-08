#!/bin/bash
until curl -sS "http://localhost:9200/_cat/health?h=status" | grep -q "green\|yellow"; do
        sleep 1
done

echo "ENTRYPOINT: Creating indexes ..."

curl -XPUT 'http://localhost:9200/geoss-cr' -H 'Content-Type: application/json' -d @/indices/geoss-cr-index.json
curl -XPUT 'http://localhost:9200/geoss-cr-extensions' -H 'Content-Type: application/json' -d @/indices/geoss-cr-extensions-index.json
curl -XPUT 'http://localhost:9200/geoss-recommendation' -H 'Content-Type: application/json' -d @/indices/geoss-recommendation-index.json
curl -XPUT 'http://localhost:9200/thesaurus-vocabulary' -H 'Content-Type: application/json' -d @/indices/thesaurus-vocabulary-index.json

echo "ENTRYPOINT: Indexes created"
