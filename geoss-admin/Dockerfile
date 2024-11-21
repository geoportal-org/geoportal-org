FROM node:18.18.2-alpine

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-admin"

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en

ENV NODE_ENV=production

RUN apk upgrade --no-cache

USER 1000
WORKDIR /home/node

COPY --chown=1000 . .
COPY --chown=1000 entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

RUN npm install --production
#RUN npm run build

EXPOSE 3000

ENTRYPOINT ["/entrypoint.sh"]
CMD [ "npm", "run", "start" ]