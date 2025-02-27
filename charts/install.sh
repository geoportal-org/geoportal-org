## Environment configuration
Red='\033[0;31m'          # Red
Green='\033[0;32m'        # Green
Yellow='\033[0;33m'       # Yellow
NC='\033[0m' # No Color

# Load variables (if .env file exists)
source .env

# Conditional logic
export MAILDEV_RELAY_HOST="${MAIL_HOST}"
export MAILDEV_RELAY_PORT="${MAIL_PORT:=587}"
export MAILDEV_RELAY_AUTH="login"
export MAILDEV_RELAY_USERNAME="${MAIL_USERNAME:=geoss@example.com}"
export MAILDEV_RELAY_PASSWORD="${MAIL_PASSWORD}"

export MAILDEV_RELAY_ALL="false"

if [ -n "$MAIL_HOST" ] && [ -n "$MAIL_PORT" ] && [ -n "$MAIL_USERNAME" ] && [ -n "$MAIL_PASSWORD" ]
then
    export MAILDEV_RELAY_ALL="true"
fi

export SMTP_HOST="${RESOURCE_NAME_PREFIX}-maildev"
export SMTP_PORT="1025"
export SMTP_USER="maildev"
export SMTP_PASSWORD="${MAILDEV_SMTP_PASSWORD}"

export CSP_DOMAINS="${UI_DOMAIN_NAME},${IDP_DOMAIN_NAME},${ADMIN_DOMAIN_NAME}"

## Installation steps

printf "\n\n ${Green}Deploy GEOSS-els ...\n\n${NC}"
envsubst < geoss-els/values.yaml.template > geoss-els/values.yaml
helm -n $K8S_NAMESPACE upgrade --install --wait --timeout 10m0s\
    --debug $RESOURCE_NAME_PREFIX-els geoss-els | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-db ...\n\n${NC}"
envsubst < geoss-db/values.yaml.template > geoss-db/values.yaml
helm -n $K8S_NAMESPACE upgrade --install --wait --timeout 10m0s \
    --debug $RESOURCE_NAME_PREFIX-db geoss-db | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-keycloak ...\n\n${NC}"
envsubst < geoss-keycloak/values.yaml.template > geoss-keycloak/values.yaml
helm -n $K8S_NAMESPACE upgrade --install --wait --timeout 10m0s\
    --debug $RESOURCE_NAME_PREFIX-keycloak geoss-keycloak | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

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

printf "\n\n ${Green}Deploy GEOSS-kibana ...\n\n${NC}"
envsubst < geoss-kibana/values.yaml.template > geoss-kibana/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-kibana geoss-kibana | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-landingpage ...\n\n${NC}"
envsubst < geoss-landingpage/values.yaml.template > geoss-landingpage/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-landingpage geoss-landingpage | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-maildev ...\n\n${NC}"
envsubst < geoss-maildev/values.yaml.template > geoss-maildev/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-maildev geoss-maildev | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

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

printf "\n\n ${Green}Deploy GEOSS-ui ...\n\n${NC}"
envsubst < geoss-ui/values.yaml.template > geoss-ui/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-ui geoss-ui | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-worker-geodab-worker ...\n\n${NC}"
envsubst < geoss-worker-geodab-worker/values.yaml.template > geoss-worker-geodab-worker/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-worker-geodab-worker geoss-worker-geodab-worker | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-worker-sdg-worker ...\n\n${NC}"
envsubst < geoss-worker-sdg-worker/values.yaml.template > geoss-worker-sdg-worker/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-worker-sdg-worker geoss-worker-sdg-worker | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-worker-thesaurus-worker ...\n\n${NC}"
envsubst < geoss-worker-thesaurus-worker/values.yaml.template > geoss-worker-thesaurus-worker/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-worker-thesaurus-worker geoss-worker-thesaurus-worker | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

printf "\n\n ${Green}Deploy GEOSS-worker-wikipedia-worker ...\n\n${NC}"
envsubst < geoss-worker-wikipedia-worker/values.yaml.template > geoss-worker-wikipedia-worker/values.yaml
helm -n $K8S_NAMESPACE upgrade --install \
    --debug $RESOURCE_NAME_PREFIX-worker-wikipedia-worker geoss-worker-wikipedia-worker | grep -E "(Happy\ Helming|NAME\: |LAST DEPLOYED\: |NAMESPACE\: |STATUS\: |REVISION\: | TEST SUITE\: )"  || true

