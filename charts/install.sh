Red='\033[0;31m'          # Red
Green='\033[0;32m'        # Green
Yellow='\033[0;33m'       # Yellow
NC='\033[0m' # No Color

source .env

printf "\n\n ${Green}Deploy GEOSS-admin ...\n\n${NC}"
envsubst < geoss-admin/values.yaml.template > geoss-admin/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-admin geoss-admin | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-contents ...\n\n${NC}"
envsubst < geoss-contents/values.yaml.template > geoss-contents/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-contents geoss-contents | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-curated ...\n\n${NC}"
envsubst < geoss-curated/values.yaml.template > geoss-curated/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-curated geoss-curated | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-db ...\n\n${NC}"
envsubst < geoss-db/values.yaml.template > geoss-db/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-db geoss-db | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-kibana ...\n\n${NC}"
envsubst < geoss-kibana/values.yaml.template > geoss-kibana/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-kibana geoss-kibana | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-matomo ...\n\n${NC}"
envsubst < geoss-matomo/values.yaml.template > geoss-matomo/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-matomo geoss-matomo | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

cp *.crt geoss-nginx/.
cp *.key geoss-nginx/.
printf "\n\n ${Green}Deploy GEOSS-nginx ...\n\n${NC}"
envsubst < geoss-nginx/values.yaml.template > geoss-nginx/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-nginx geoss-nginx | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-personaldata ...\n\n${NC}"
envsubst < geoss-personaldata/values.yaml.template > geoss-personaldata/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-personaldata geoss-personaldata | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-proxy ...\n\n${NC}"
envsubst < geoss-proxy/values.yaml.template > geoss-proxy/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-proxy geoss-proxy | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-search ...\n\n${NC}"
envsubst < geoss-search/values.yaml.template > geoss-search/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-search geoss-search | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-settings ...\n\n${NC}"
envsubst < geoss-settings/values.yaml.template > geoss-settings/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-settings geoss-settings | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true
