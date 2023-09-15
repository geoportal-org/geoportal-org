# GEOSS

GEOSS

## Author

Eversis Sp.z o.o.
ul. Lirowa 13,
02-387 Warszawa, Polska
tel: +48 22 578 55 00
fax: +48 22 824 25 23
[biuro@eversis.com](mailto:biuro@eversis.com)
[eversis.com](http://eversis.com/)

## Requirements

- `Docker`

## Modules

### [Git Hooks](git-hooks/README.md)

Hooks to git and gitlab.

### [GEOSS Admin](geoss-admin/README.md)

Frontend graphic interface for user, contents, settings, etc. (Next.js / React)

### [GEOSS Bookmarks](geoss-bookmarks/README.md)

Module for Bookmarks and GEO Likes storage and management. (Java / Spring)

### [GEOSS Contents](geoss-contents/README.md)

Module for Contents management and Files repository. (Java / Spring)

### [GEOSS DB](geoss-db/README.md)

Database module. (MariaDB)

### [GEOSS Elasticsearch](geoss-elk/README.md)

Elasticsearch module.

### [GEOSS Keycloak](geoss-keycloak/README.md)

Authorization provider module.

### [GEOSS Kibana](geoss-kibana/README.md)

Graphic interface for geoss-elk module.

### [GEOSS Matomo](geoss-matomo/README.md)

Matomo module.

### [GEOSS Nginx](geoss-nginx/README.md)

Nginx module.

### [GEOSS Proxy](geoss-proxy/README.md)

Proxy modlue. (Java / Spring)

### [GEOSS Settings](geoss-settings/README.md)

Module for all portal instance settings. (Java / Spring)

### [GEOSS Squid](geoss-squid/README.md)

Squid request caching module.

### [GEOSS UI](geoss-ui/README.md)

Frontend graphic interface for search, content pages, etc. (Nuxt.js / Vue)

### [GEOSS personaldata](geoss-personaldata/README.md)

Module for User data management. (Java / Spring)

## Setup local environment

### Add alias `geoss-keycloak` to localhost in host file
```
127.0.0.1	localhost geoss-keycloak
```

### Run command to start
```
docker compose -f docker-compose-local.yml -p geoss up -d
```

### Create user geoss in geoss realm
Get access token
```
curl --location --request POST 'http:///geoss-keycloak:8080/realms/master/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=admin-cli' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'username=admin' \
--data-urlencode 'password=qaz123'
```
Create user
```
curl --location --request POST 'http://geoss-keycloak:8080/admin/realms/geoss/users' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <access_token>' \
--data-raw '{"firstName":"Geoss","lastName":"Geoss","username":"geoss","email":"geoss@localhost","enabled":"true","emailVerified":"true","credentials":[{"type":"password","value":"geoss","temporary":false}],"groups":["administrator","realm-manager"]}'
```

### Keycloak
On first run keycloak import realm configuration from file geoss_realm.json, it may take a while.

After start keycloak is available on address
http://geoss-keycloak:8080/

Admin console for geoss realm is available at
http://geoss-keycloak:8080/admin/geoss/console/

Account console for geoss realm is available at
http://geoss-keycloak:8080/realms/geoss/account/

# Installation guide

## Prerequisites
Prerequisites: 
- Open network communication between servers
- Servers should be configured as working docker swarm network
- Active domains SSL certificates and keys
## System requirements for each server
## Deployment process
Eversis CI/CD pipeline will build and upload images into external images server available for clients. We will also prepare docker-compose template files available for downloads. Users will be able to download docker-compose configuration templates, edit variables and deploy application on their servers using uploaded by Eversis images.
## Deployment guide
1. Download docker-compose files
2. Edit each of the environment variables inside docker-compose-dmz.yml and docker-compose-lan.yml files
	- Variable1
	- Variable2
	- ...
3. Copy SSL files(certificate+key) into listed directories:
	- Path1
	- Path2
	- ...
4. Copy docker-compose files into docker swarm manager servers
```
scp -P $VAR_SSH_PORT ./$VAR_DOCKER_COMPOSE_FILE_DMZ $VAR_MANAGER_SSH_CONNECTION_DMZ:/tmp/$VAR_DOCKER_COMPOSE_FILE_DMZ
scp -P $VAR_SSH_PORT ./$VAR_DOCKER_COMPOSE_FILE_LAN $VAR_MANAGER_SSH_CONNECTION_LAN:/tmp/$VAR_DOCKER_COMPOSE_FILE_LAN
```
5. Set environments over ssh and deploy stack to servers
```
ssh -p $VAR_SSH_PORT $VAR_MANAGER_SSH_CONNECTION_DMZ "export ... && docker stack deploy -c $VAR_DOCKER_COMPOSE_FILE_DMZ geoss"
ssh -p $VAR_SSH_PORT $VAR_MANAGER_SSH_CONNECTION_LAN "export ... && docker stack deploy -c $VAR_DOCKER_COMPOSE_FILE_LAN geoss"
```
