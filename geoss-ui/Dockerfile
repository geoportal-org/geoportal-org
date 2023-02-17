FROM node:16.19.0-alpine

COPY . .

RUN npm install --global yarn

RUN yarn --non-interactive install 
RUN yarn --non-interactive run build 
RUN yarn --non-interactive run generate 

EXPOSE 3000

CMD ["yarn", ,"--non-interactive", "run", "start"]