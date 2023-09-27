# GEOSS Curated Relationships Workers

This project consists of subprojects, which are in general responsible for fetching data from multiple, external sources.
In general they save data in SQL database, which is primary data store.
It means that this database should be always stable and backup of data should be periodically made.

Each worker project is responsible for different area of interest and may be running with different schedule.

In general next step is loading data from SQL database to Elasticsearch, which is done by Logstash.
Proper scripts are maintained by Ansible.

In general project consists of several components:

* [entry-worker](entry-worker) - project containing all workers related to main resources stored in primary data-store
* [thesaurus-worker](thesaurus-worker) - worker, which fetches thesaurus data and loads it directly into Elasticsearch
