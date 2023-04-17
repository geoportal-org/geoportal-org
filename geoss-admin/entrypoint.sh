#!/bin/sh

echo "NEXT_PUBLIC_API=$NEXT_PUBLIC_API" > .env.production

npm run build

exec "$@"