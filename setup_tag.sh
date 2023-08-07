#!/bin/sh

search_for="release-"

echo "ref_name: $CI_COMMIT_REF_NAME"
echo "short_sha: $CI_COMMIT_SHORT_SHA"

if [ "develop" = "$CI_COMMIT_REF_NAME" ]
then
  export DOCKER_IMAGE_TAG=latest
elif [ "uat" = "$CI_COMMIT_REF_NAME" ]
then
  export DOCKER_IMAGE_TAG=$CI_COMMIT_REF_NAME-$CI_COMMIT_SHORT_SHA
elif [ "1" -eq "$(echo "${CI_COMMIT_REF_NAME}" | grep -c $search_for)" ]
then
  export DOCKER_IMAGE_TAG=${CI_COMMIT_REF_NAME##*-}
else
  # Set default value
  export DOCKER_IMAGE_TAG=$CI_COMMIT_SHORT_SHA
fi
