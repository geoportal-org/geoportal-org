#!/bin/bash

if [ ! "${REDIRECT_ENABLED}" = "true" ]; then
  rm /etc/nginx/conf.d/gpp-redirect.conf
fi