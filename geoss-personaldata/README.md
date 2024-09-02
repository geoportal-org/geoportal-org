# [GEOSS personaldata](geoss-personaldata/README.md)

Module for all portal instance personaldata. (Java / Spring)
Backend application provided REST API to frontend.
The application is based on the **Spring Boot 3 framework**.

The REST API is built using the [HATEOAS](https://en.wikipedia.org/wiki/HATEOAS) principles.
The resource presentation format is the [HAL](https://en.wikipedia.org/wiki/Hypertext_Application_Language) standard, it is used as an implementation of HATEOAS.

REST API provides public and private methods.
Access to private data requires authorization.

## Prerequisites

- `Java 21`

## Build Project

```shell
cd middleware
./gradlew clean assemble
```

## Build Docker Image

```shell
cd middleware
docker build -t inconada/middleware .
```
or build image and tag it with current project version
```shell
docker build -t inconada/middleware:`cat gradle.properties | grep version | awk -F= '{ print $2 }'` .
```

## Configuration environment

| Environment variable                | Description                              |
|-------------------------------------|------------------------------------------|
| SPRING_DATASOURCE_DRIVER_CLASS_NAME | Fully qualified name of the JDBC driver. |
| SPRING_DATASOURCE_URL               | JDBC URL of the database                 |
| SPRING_DATASOURCE_USERNAME          | Login username of the database           |
| SPRING_DATASOURCE_PASSWORD          | Login password of the database           |
| SPRING_SECURITY_USER_NAME           | Default user name                        |
| SPRING_SECURITY_USER_PASSWORD       | Password for the default user name       |
| SPRING_SECURITY_USER_ROLES          | Granted roles for the default user       |
| SPRING_SECURITY_USER_EMAIL          | Email for the default user               |
| SPRING_SECURITY_USER_FIRST_NAME     | First name for the default user          |
| SPRING_SECURITY_USER_LAST_NAME      | Last name for the default user           |
| SPRING_MAIL_HOST                    | SMTP server host                         |
| SPRING_MAIL_PORT                    | SMTP server port                         |
| SPRING_MAIL_USERNAME                | Login user of the SMTP server (email)    |
| SPRING_MAIL_PASSWORD                | Login password of the SMTP server        |

The general format for a password is: {id}encodedPassword
See the documentation for details
[DelegatingPasswordEncoder](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/password/DelegatingPasswordEncoder.html)
[PasswordEncoderFactories](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/factory/PasswordEncoderFactories.html)

For administrator access, assign the role: ROLE_ADMIN

## Setup local environment

To start the application run command
```shell
cd middleware
./gradlew clean assemble
./gradlew bootRun --args='--spring.profiles.active=local'
```

After started, application is available on default port 8080
http://localhost:8080

Default user:
```
username: user
password: qaz123
```

By default, the application uses the memory database (H2)

|            |                    |
|------------|--------------------|
| JDBC URL:  | jdbc:h2:mem:testdb |
| User Name: | sa                 |
| Password:  |                    |

Available tools in application

- [Swagger UI](http://localhost:8080/swagger-ui/index.html)

- [H2 console](http://localhost:8080/h2-console/)

Dedicated configuration for local environment is in the file
[application-local.properties](application/src/main/resources/application-local.properties)

## Application modules

### application

### common
