#!/bin/bash

if [ "${MAILDEV_ENABLED}" = "true" ]; then
  mv /etc/nginx/conf.d/gpp-admin.conf /etc/nginx/conf.d/gpp-admin.conf.old
  awk -v r="" '{gsub(/#MAILDEV#/,r)}1' /etc/nginx/conf.d/gpp-admin.conf.old > /etc/nginx/conf.d/gpp-admin.conf
  rm /etc/nginx/conf.d/gpp-admin.conf.old
fi