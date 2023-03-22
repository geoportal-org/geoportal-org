FROM catatnight/postfix

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-postfix"

ARG ENVIRONMENT=""


RUN sed -i "s/127.0.0.0\/8/10.0.0.0\/8 172.0.0.0\/8 127.0.0.0\/8/g" /etc/postfix/main.cf

COPY scripts/overwrite_recipients.sh /overwrite_recipients.sh
COPY scripts/wait-for.sh /wait-for.sh
COPY scripts/install.sh /opt/install.sh

RUN chmod +x /overwrite_recipients.sh
RUN chmod +x /wait-for.sh
RUN chmod +x /opt/install.sh

HEALTHCHECK \
	--interval=30s \
	--start-period=30s \
	--timeout=1m \
CMD /wait-for.sh localhost:25


CMD /overwrite_recipients.sh;/opt/install.sh;/usr/bin/supervisord -c /etc/supervisor/supervisord.conf

