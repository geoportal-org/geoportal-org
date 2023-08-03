#!/bin/bash

/create_index_patterns.sh&
/opt/bitnami/scripts/kibana/entrypoint.sh $1
