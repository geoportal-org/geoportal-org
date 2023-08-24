#!/bin/bash

/initialize_els.sh&
/bin/tini -- /usr/local/bin/docker-entrypoint.sh $1