FROM node:16.19.0-alpine

COPY . .

RUN yarn --non-interactive install 
RUN yarn --non-interactive run build 

EXPOSE 3000

CMD ["yarn", "--non-interactive", "run", "start"]