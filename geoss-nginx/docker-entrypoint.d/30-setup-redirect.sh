#!/bin/bash

if [ ! "${REDIRECT_ENABLED}" = "true" ]; then
  rm -f /etc/nginx/conf.d/gpp-redirect.conf
fi