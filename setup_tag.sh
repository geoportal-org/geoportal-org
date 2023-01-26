#!/bin/sh

# Set default value
export DOCKER_IMAGE_TAG=latest
search_for="release-"

if [ "1" -eq "$(echo ${CI_COMMIT_REF_NAME} | grep -c $search_for)" ]
then
  export DOCKER_IMAGE_TAG=${CI_COMMIT_REF_NAME##*-}
fi