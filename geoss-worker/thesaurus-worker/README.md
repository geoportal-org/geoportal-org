# GEOSS Thesaurus worker

Project aim is to load concepts from Thesaurus to internal database in order to provide extended search functionalities like See also feature.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

To run this server on your local machine you need to have installed:

*  JDK 17
*  Elasticsearch server instance (7.17)

### Installing

In order to run this server, Elasticsearch database should be up and running.
Two main steps are required to run *thesaurus-worker* server.

1. Configure Elasticsearch index using proper settings and mappings.
2. Provide Elasticsearch parameters to *thesaurus-worker* configuration file.

#### Worker
Project uses Spring Boot 2.7.x with Gradle 8.x as a build tool, so minimal or no configuration is required to make this application up and running.
Before running server Elasticsearch server should be up.

Application uses reactor stack, so all API, which are used should support reactive, non-blocking API.
Worker fetches recursively concepts from external API and loads them directly to Elasticsearch.

Create runnable JAR
```
$ gradlew bootJar
```

Run *thesaurus-worker* server using Gradle
```
$ gradlew bootRun -Dspring.profiles.active=local
```

Run *thesaurus-worker* server using generated JAR
```
$ cd build/libs
$ java -jar -Dspring.profiles.active=local thesaurus-worker-0.0.1.jar
```

##### Configuration

General configuration for *thesaurus-worker* is stored in [application.properties](src/main/resources/application.properties).
Profile specific configurations are located in *application-{PROFILE}.properties* files.

Elasticsearch parameters (like hostname, port and index parameters) are stored in profile specific configuration files.
