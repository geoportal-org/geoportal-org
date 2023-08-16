#!/bin/bash

/create_indexes.sh&

echo "Adding keystore value"
echo -n "xTUIINx6U7qe6lcTcrRUdgYqVKo08dXs" | bin/elasticsearch-keystore add --stdin xpack.security.authc.realms.oidc.geoss.rp.client_secret
bin/elasticsearch-keystore show xpack.security.authc.realms.oidc.geoss.rp.client_secret
echo "Added keystore value"

/bin/tini -- /usr/local/bin/docker-entrypoint.sh $1