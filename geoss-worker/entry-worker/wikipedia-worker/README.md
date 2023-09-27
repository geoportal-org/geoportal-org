# GEOSS Wikipedia worker

Project aim is to load basic Wikipedia data from speficied categories to internal database in order to provide search capabilities, which are not available by official Wikipedia API.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

To run this server on your local machine you need to have installed:

*  JDK 17
*  MySQL server instance (5.7 or above)

### Installing

In order to run this server, MySQL database should be up and running.
Two main steps are required to run *wikipedia-worker* server.

1. Configure database using proper schema.
2. Provide database parameters to *wikipedia-worker* configuration file.

#### Database configuration

Database schema is located in geoss-curatec project.

#### Worker
Project uses Spring Boot 2.7.x with Gradle 8.x as a build tool, so minimal or no configuration is required to make this application up and running.
Before running server MySQL server should be up.

In order to create stable and scalable batch solution Spring Batch library was used.
The whole application consists of single job, which contains 2 sequential steps.

1. Step - load recursively categories basing on provided configuration. Category list is stored in a in-memory database.
2. Step - fetch articles using Wikipedia API, which are part of previously loaded categories. Downloaded data is processed and stored in target database.

Create runnable JAR
```
$ gradlew bootJar
```

Run *wikipedia-worker* server using generated JAR
```
$ cd build/libs
$ java -jar -Dspring.profiles.active=local wikipedia-worker-0.0.1.jar --spring.datasource.password=root
```

##### Configuration

General configuration for *wikipedia-worker* is stored in [application.properties](src/main/resources/application.properties).
Profile specific configurations are located in *application-{PROFILE}.properties* files.

Database parameters (like hostname or username) are stored in profile specific configuration files.
