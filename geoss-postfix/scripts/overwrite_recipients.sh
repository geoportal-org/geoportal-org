#!/bin/bash


if [[ ! -z "$OVERWRITE_RECIPIENTS_BY" ]]; then

echo "recipient_canonical_maps = regexp:/etc/postfix/recipient_canonical" >> /etc/postfix/main.cf
echo "/^.*/  $OVERWRITE_RECIPIENTS_BY" > /etc/postfix/recipient_canonical

echo "$(date '+%Y-%m-%d %H:%M:%S') All email will send to $OVERWRITE_RECIPIENTS_BY"
fi
