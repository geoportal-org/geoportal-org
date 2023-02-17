FROM node:16.19.0-alpine

COPY . .

RUN yarn --non-interactive install 
RUN echo fs.inotify.max_user_watches=524288 | tee -a /etc/sysctl.conf && sysctl -p
RUN yarn --non-interactive run build 
RUN yarn --non-interactive run generate 

EXPOSE 3000

CMD ["yarn", "--non-interactive", "run", "start"]