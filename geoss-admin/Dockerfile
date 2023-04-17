FROM node:16.19.0-alpine

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-admin"

ENV NODE_ENV=production

COPY . .
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
RUN ls -alh /

RUN npm install --production
#RUN npm run build

EXPOSE 3000

ENTRYPOINT ["/entrypoint.sh"]
CMD [ "npm", "run", "start" ]