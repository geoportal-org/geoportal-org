# [GEOSS Contents](geoss-contents/README.md)

Module for Contents management and Files repository. (Java / Spring)
Backend application provided REST API to frontend.
The application is based on the **Spring Boot 3 framework**.

The REST API is built using the [HATEOAS](https://en.wikipedia.org/wiki/HATEOAS) principles.
The resource presentation format is the [HAL](https://en.wikipedia.org/wiki/Hypertext_Application_Language) standard, it is used as an implementation of HATEOAS.

## Prerequisites

- `Java 17`
## Build project

```shell
cd geoss-contents
./gradlew clean assemble
```

## Build Docker local image

```shell
cd geoss-contents
docker build -f Dockerfile-local -t geoss/geoss-contents .
```

## Run the container from image
```shell
cd geoss-contents
docker run -p8080:8080 geoss/geoss-contents:latest
```

By default, the application uses the memory database (H2)

|            |                               |
|------------|-------------------------------|
| JDBC URL:  | jdbc:h2:mem:geoss_contents_db |
| User Name: | sa                            |
| Password:  |                               |

Available tools in application

- [H2 console](http://localhost:8080/h2-console/)

- [Swagger UI](http://localhost:8080/swagger-ui/index.html)
