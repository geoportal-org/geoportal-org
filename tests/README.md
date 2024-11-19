# GEOSS performance and load testing

For measuring performance [Gatling](https://gatling.io/) tool has been used.
It uses Scala DSL for defining test scenarios.

## Prerequisites

- `Java 8`

## Usage

Please note that in order to run performance test you may need to disabled DOS protection.
For example in Earth Online you need to disable mod_evasive in Apache Webserver.

- In order to run all scenarios with default parameters run:
    ```
    ./mvnw gatling:test
    ```

- In order tu run only single scenario run:
    ```
    ./mvnw gatling:test -Dgatling.simulationClass=$SCENARIO_FULL_CLASSNAME$
    ```

  for example:
    ```
    ./mvnw gatling:test -Dgatling.simulationClass=simulations.geoportal.GEOSSBasicSimulation
    ```

- Some scenario may allow specifying parameters as command line arguments (check scenario implementation)

- Example for running performance test for Monthly report for ESA

  - OLD GEOSS UAT
    ```
    ./mvnw gatling:test -f load-test/pom.xml -Dgatling.simulationClass=simulations.geoportal.GEOSSBasicSimulation -DbaseUrl=https://geoss.uat.esaportal.eu
    ```
  - GEOSS UAT
    ```
    ./mvnw gatling:test -f load-test/pom.xml -Dgatling.simulationClass=simulations.geoss.GeossBasicSimulation -DuiBaseUrl=https://gpp.uat.esaportal.eu -DidpBaseUrl=https://gpp-idp.uat.esaportal.eu
    ```
  - GEOSS PROD
    ```
    ./mvnw gatling:test -f load-test/pom.xml -Dgatling.simulationClass=simulations.geoss.GeossBasicSimulation -DuiBaseUrl=https://www.geoportal.org -DidpBaseUrl=https://idp.geoportal.app
    ```
