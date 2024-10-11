Red='\033[0;31m'          # Red
Green='\033[0;32m'        # Green
Yellow='\033[0;33m'       # Yellow
NC='\033[0m' # No Color

source .env

cp *.crt geoss-nginx/.
cp *.key geoss-nginx/.
printf "\n\n ${Green}Deploy GEOSS-nginx ...\n\n${NC}"
envsubst < geoss-nginx/values.yaml.template > geoss-nginx/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-nginx geoss-nginx | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-kibana ...\n\n${NC}"
envsubst < geoss-kibana/values.yaml.template > geoss-kibana/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-kibana geoss-kibana | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

