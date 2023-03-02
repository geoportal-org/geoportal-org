FROM ubuntu/squid:4.10-20.04_beta as runner

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-squid"

ENV TZ=UTC

# Update libraries
RUN apt-get update
# Install modsecurity-helper libraries
RUN apt-get install -y netcat apache2-utils
# Cleanup
RUN rm -rf /var/lib/apt


COPY etc/squid /etc/squid
COPY usr/local/bin /usr/local/bin
RUN chmod +x /usr/local/bin/entrypoint.sh