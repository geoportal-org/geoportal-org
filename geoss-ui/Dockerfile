FROM node:16.19.1-slim


COPY package*.json .

RUN yarn --non-interactive install 

COPY . .

RUN yarn --non-interactive run build 

EXPOSE 3000

CMD ["yarn", "--non-interactive", "run", "start"]