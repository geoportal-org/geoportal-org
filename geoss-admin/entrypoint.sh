#!/bin/sh

echo "NEXT_PUBLIC_API=$NEXT_PUBLIC_API
NEXT_PUBLIC_APP_URL=$NEXT_PUBLIC_APP_URL
NEXT_PUBLIC_URL=$NEXT_PUBLIC_URL
NEXTAUTH_URL=$NEXTAUTH_URL
NEXTAUTH_SECRET=$NEXTAUTH_SECRET
KEYCLOAK_CLIENT_ID=$KEYCLOAK_CLIENT_ID
KEYCLOAK_CLIENT_SECRET=$KEYCLOAK_CLIENT_SECRET
KEYCLOAK_BASE_URL=$KEYCLOAK_BASE_URL" > .env.production

npm run build

exec "$@"