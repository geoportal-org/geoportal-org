#!/bin/bash

/create_indexes.sh&
/bin/tini -- /usr/local/bin/docker-entrypoint.sh $1