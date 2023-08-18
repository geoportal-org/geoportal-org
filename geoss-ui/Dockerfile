FROM node:16.19.1-slim

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-ui"

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en

COPY package*.json .

RUN yarn --non-interactive install 

COPY . .

RUN yarn --non-interactive run build 

EXPOSE 3000

CMD ["yarn", "--non-interactive", "run", "start"]