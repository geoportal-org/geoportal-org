#!/bin/bash

#judgement
if [[ -a /etc/supervisor/conf.d/supervisord.conf ]]; then
  exit 0
fi

#supervisor
cat > /etc/supervisor/conf.d/supervisord.conf <<EOF
[supervisord]
nodaemon=true
[program:postfix]
command=/opt/postfix.sh
[program:rsyslog]
command=/usr/sbin/rsyslogd -n -c3
EOF

############
#  postfix
############
cat >> /opt/postfix.sh <<EOF
#!/bin/bash
service postfix start
tail -f /var/log/mail.log
EOF
chmod +x /opt/postfix.sh
postconf -F '*/*/chroot = n'

if [ -z ${OVERWRITE_SENDER_BY} ]; then
  echo "OVERWRITE_SENDER_BY not set skip overide FROM header";
else
  echo "/^.*/ $OVERWRITE_SENDER_BY" > /etc/postfix/sender_canonical
  echo "/From:.*/ REPLACE From: $OVERWRITE_SENDER_BY" > /etc/postfix/header_check
fi

if [ -z ${OVERWRITE_RECIPIENTS_BY} ]; then 
  echo "OVERWRITE_RECIPIENTS_BY not set skip overide TO header"; 
else  
  echo "/TO:.*/ REPLACE TO: $OVERWRITE_RECIPIENTS_BY" >> /etc/postfix/header_check; 
  echo "/CC:.*/ REPLACE CC: $OVERWRITE_RECIPIENTS_BY" >> /etc/postfix/header_check; 
  echo "/BCC:.*/ REPLACE BCC: $OVERWRITE_RECIPIENTS_BY" >> /etc/postfix/header_check; 
fi


############
# SASL SUPPORT FOR CLIENTS
# The following options set parameters needed by Postfix to enable
# Cyrus-SASL support for authentication of mail clients.
############
# /etc/postfix/main.cf
echo "Setting up main.cf"
postconf -e relayhost=$DMZ_RELAYHOST
postconf -e smtp_sasl_security_options=
postconf -e sender_canonical_maps=regexp:/etc/postfix/sender_canonical
postconf -e smtp_header_checks=regexp:/etc/postfix/header_check