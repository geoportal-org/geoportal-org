source .env

helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-admin
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-contents
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-curated
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-db
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-els
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-keycloak
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-kibana
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-landingpage
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-maildev
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-matomo
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-nginx
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-personaldata
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-proxy
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-search
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-settings
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-ui
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-worker-geodab-worker
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-worker-sdg-worker
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-worker-thesaurus-worker
helm -n $K8S_NAMESPACE delete $RESOURCE_NAME_PREFIX-worker-wikipedia-worker
