# [GEOSS Proxy](geoss-proxy/README.md)

Module for get popularWord and log/get statistics to the elasticsearch. (Java / Spring)
Backend application provided REST API to frontend.
The application is based on the **Spring Boot 3 framework**.

## Prerequisites

- `Java 21`
## Build project

```shell
cd geoss-proxy
./gradlew clean assemble
```

## Build Docker local image

```shell
cd geoss-proxy
docker build -f Dockerfile-local -t geoss/geoss-proxy .
```

## Run the container from image
```shell
cd geoss-proxy
docker run -p8080:8080 geoss/geoss-proxy:latest
```

Available tools in application
- [Swagger UI](http://localhost:8082/swagger-ui/index.html)
