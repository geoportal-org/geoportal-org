source .env

helm delete $RESOURCE_NAME_PREFIX-admin
helm delete $RESOURCE_NAME_PREFIX-contents
helm delete $RESOURCE_NAME_PREFIX-curated
helm delete $RESOURCE_NAME_PREFIX-db
helm delete $RESOURCE_NAME_PREFIX-kibana
helm delete $RESOURCE_NAME_PREFIX-matomo
helm delete $RESOURCE_NAME_PREFIX-nginx
helm delete $RESOURCE_NAME_PREFIX-personaldata
helm delete $RESOURCE_NAME_PREFIX-proxy
helm delete $RESOURCE_NAME_PREFIX-search
helm delete $RESOURCE_NAME_PREFIX-settings
helm delete $RESOURCE_NAME_PREFIX-worker-geodab-worker
helm delete $RESOURCE_NAME_PREFIX-worker-sdg-worker
helm delete $RESOURCE_NAME_PREFIX-worker-thesaurus-worker
helm delete $RESOURCE_NAME_PREFIX-worker-wikipedia-worker