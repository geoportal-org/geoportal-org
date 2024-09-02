# [GEOSS Keycloak](geoss-keycloak/README.md)

Authorization provider module.

## Prerequisites

- `Java 21`

## Setup local environment

### Build providers

```shell
cd geoss-keycloak
./gradlew clean assemble
```

### Build Docker Image

```shell
cd geoss-keycloak/
docker build -f Dockerfile -t geoss-keycloak .
```

### Run keycloak with mariadb

In file `docker-compose-local.yaml` uncomment this lines to mount local theme directory
```
#        volumes:
#            - "./geoss-keycloak/themes/geoss:/opt/keycloak/themes/geoss"
```

Go to project root directory and run command
```shell
docker compose -f docker-compose-local.yml -p geoss up -d geoss-db geoss-keycloak
```
