# GEOSS Entry workers

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

To run this server on your local machine you need to have installed:

*  JDK 17
*  MySQL server instance (5.7 or above)

### Installing

In order to run this server, MySQL database should be up and running.
Two main steps are required to run *wikipedia-worker* server.

1. Configure database using proper schema
    - run geoss-curated application to run liquibase scripts
2. Provide database parameters to configuration file (typically application.properties) file.

#### Database configuration

Database schema is located in geoss-curated project.

#### Worker
Projects uses Spring Boot 2.7.x with Gradle 8.x as a build tool, so minimal or no configuration is required to make this application up and running.
Before running server MySQL server should be up.

In order to create stable and scalable batch solution Spring Batch library was used.
Applications consist of single job, which contain multiple steps (depending on domain area).

Each worker is run as a standalone console application. It may be run via cron job on target environment.

In order to create runnable JAR for specific worker e.g. wikipedia-worker, go to its directory and run:
```
$ gradlew bootJar
```

In order to run application (in this example *wikipedia-worker*) go to its directory and run:
```
$ cd build/libs
$ java -jar -Dspring.profiles.active=local wikipedia-worker-0.0.1.jar --spring.datasource.password=root
```

Depending on configuration, worker application may save metadata about job in target database.
You can find there details if job was completed, how long it took etc.

##### Configuration

Profile specific configurations are located in *application-{PROFILE}.properties* files.

Database parameters (like hostname or username) are stored in profile specific configuration files.
