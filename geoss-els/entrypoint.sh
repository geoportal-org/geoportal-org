#!/bin/bash

until curl -sS "http://localhost:9200/_cat/health?h=status" | grep -q "green\|yellow"; do
        sleep 1
done

curl -XPUT 'http://localhost:9200/geoss_cr' -H "Content-Type: application/json" -d @/tmp/geoss-cr-mappings.json
curl -XPUT 'http://localhost:9200/geoss_cr' -H "Content-Type: application/json" -d @/tmp/geoss-cr-settings.json
curl -XPUT 'http://localhost:9200/geoss_cr_extensions' -H "Content-Type: application/json" -d @/tmp/geoss-cr-extensions-mappings.json
curl -XPUT 'http://localhost:9200/geoss_cr_extensions' -H "Content-Type: application/json" -d @/tmp/geoss-cr-extensions-settings.json
curl -XPUT 'http://localhost:9200/geoss-recommendation' -H "Content-Type: application/json" -d @/tmp/geoss-recommendation-mappings.json
curl -XPUT 'http://localhost:9200/geoss-recommendation' -H "Content-Type: application/json" -d @/tmp/geoss-recommendation-settings.json