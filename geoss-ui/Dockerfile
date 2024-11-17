FROM node:18.12-alpine AS dependencies

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en
#ENV NODE_ENV=production

WORKDIR /opt/app

COPY package.json yarn.lock ./

RUN yarn install --frozen-lockfile --non-interactive

FROM node:18.12-alpine AS builder

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en
ENV NODE_ENV=production

WORKDIR /opt/app

COPY --from=dependencies /opt/app/node_modules ./node_modules
COPY --from=dependencies /opt/app/package.json ./
COPY --from=dependencies /opt/app/yarn.lock ./
COPY api ./api
COPY assets ./assets
COPY components ./components
COPY data ./data
COPY directives ./directives
COPY filters ./filters
COPY icons ./icons
COPY interfaces ./interfaces
COPY layouts ./layouts
COPY pages ./pages
COPY plugins ./plugins
COPY scheme ./scheme
COPY services ./services
COPY static ./static
COPY store ./store
COPY translations ./translations
COPY utils ./utils
COPY nuxt.config.js .
COPY tsconfig.json .

RUN yarn run build --non-interactive

FROM node:18.12-slim AS runner

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en
ENV NODE_ENV production

WORKDIR /opt/app

COPY --from=builder /opt/app/.nuxt ./.nuxt
COPY --from=builder /opt/app/node_modules ./node_modules
COPY --from=builder /opt/app/api ./api
COPY --from=builder /opt/app/assets ./assets
COPY --from=builder /opt/app/components ./components
COPY --from=builder /opt/app/data ./data
COPY --from=builder /opt/app/directives ./directives
COPY --from=builder /opt/app/filters ./filters
COPY --from=builder /opt/app/icons ./icons
COPY --from=builder /opt/app/interfaces ./interfaces
COPY --from=builder /opt/app/layouts ./layouts
COPY --from=builder /opt/app/pages ./pages
COPY --from=builder /opt/app/plugins ./plugins
COPY --from=builder /opt/app/scheme ./scheme
COPY --from=builder /opt/app/services ./services
COPY --from=builder /opt/app/static ./static
COPY --from=builder /opt/app/store ./store
COPY --from=builder /opt/app/translations ./translations
COPY --from=builder /opt/app/utils ./utils
COPY --from=builder /opt/app/package.json ./package.json
COPY --from=builder /opt/app/nuxt.config.js ./nuxt.config.js

RUN apt update && apt upgrade -y && apt clean

RUN adduser -u 1500 --disabled-password --gecos "" docker_noroot
RUN chown -R docker_noroot:docker_noroot /opt/app
USER docker_noroot 

EXPOSE 3000

ENTRYPOINT ["yarn", "run", "start"]
CMD ["--non-interactive"]
