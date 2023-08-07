# GEOSS Curated Relationships Search Server

Project aims to provide search capabilities for GEOSS Curated relationships resources.
For the sake of brevity, this project is referenced as *geoss-search* server.
Currently resources are provided by Opensearch API.

Search server has 3 main usages:
- proxy server to external data sources - it parses their search results and maps to Opensearch format
- API to GEOSS Curated resources - accesses data in Elasticsearch and maps to Opensearch format
- REST API for GEOSS resources like concepts (See also) and recommendations

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
See deployment for notes on how to deploy the project on a live system.

### Prerequisites

To run this server on your local machine you need to have installed:

- `Java 17`
- `Elasticsearch 8.9.0`

Currently, the connection to Elasticsearch 8 is provided through RestHighLevelClient with API compatibility mode.
This should be rewritten to the new Java Elasticsearch Client in the future.

### Installing

In order to run this server, Elasticsearch cluster should be running.
Two main steps are required to run *geoss-search* server.

1. Configure elasticsearch instance using proper settings and mappings.
2. Provide elasticsearch parameters to *geoss-search* configuration file.

#### Elasticsearch configuration

For development purposes it is advised to use Docker to run Elasticsearch database.
Elasticsearch settings and mappings, which should be used are stored in *geoss-els* in root directory.
```
$ cd ..
$ cd geoss-els
```

Each directory corresponds to separate index, e.g. `geoss-cr` directory contains settings and mapping of `geoss-cr` index.
All indices must be properly configured in order to run _geoss-search_ component.

In order to create index you can use Elasticsearch Index API for example:
```
PUT geoss-cr
{
    "settings" : {
        "number_of_shards" : 1
    },
    "mappings" : {
        "_doc" : {
            "properties" : {
                "field1" : { "type" : "text" }
            }
        }
    }
}
```

Note that for debugging purposes you can connect to external Elasticsearch (e.g. on DEV machine).
You just need to provide proper IP and port values in your configuration file.

#### Search server
Project uses Spring Boot 3.1.x with Gradle 8.x as a build tool, so minimal or no configuration is required to make this application up and running.
Before running server Elasticsearch cluster should be up.

Create runnable JAR
```
$ gradlew bootJar
```

Run *geoss-search* server using generated JAR
```
$ cd build/libs
$ java -jar -Dspring.profiles.active=local geoss-search-0.0.1.jar
```

##### Configuration

General configuration for *geoss-search* server is stored in [application.properties](src/main/resources/application.properties).

You should use **local** profile on your local environment - [application-local.properties](src/main/resources/application-local.properties)

Elasticsearch server parameters (like address or index name) are stored in profile specific configuration files.

On all non-local environments configuration should be managed by Ansible. It means that configuration files (application.yml and logging configuration)
should be placed outside deployed JAR file in config directory, which should be in the same directory as JAR file.

Configuration files are deployed by Ansible, which configuration is on separate Git repository. In order to change value of some property
you should edit this value in Ansible configuration, which is stored in mentioned Git repository.

In order to provide secondary backup of configuration values of **geoss-search** and in order to speed-up development process all configuration files are also stored in [config directory](config).
All changes which are made in Ansible repository should also be done within this directory.
