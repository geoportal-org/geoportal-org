#!/bin/bash

while curl -sS "http://localhost:5601" | grep -q "Kibana server is not ready yet"; do
        sleep 2
done

echo "ENTRYPOINT: Creating index-patterns"

for filename in /etc/kibana-data/*.json; do
	index_pattern_name=$(basename ${filename::-5})
	echo "ENTRYPOINT: Creating index-pattern: ${index_pattern_name}..."

	curl -X POST \
    "localhost:5601/api/saved_objects/index-pattern/${index_pattern_name}" \
    --header "kbn-xsrf: true" \
    --header "Content-Type: application/json" \
    --data @${filename}
done

echo "ENTRYPOINT: Creating index-patterns finished. Exiting."