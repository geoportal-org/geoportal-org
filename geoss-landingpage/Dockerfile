FROM node:20.16-alpine AS dependencies

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en
#ENV NODE_ENV=production

WORKDIR /opt/app

COPY package.json yarn.lock ./

RUN yarn install --frozen-lockfile --non-interactive

FROM node:20.16-alpine AS builder

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en
ENV NODE_ENV=production

WORKDIR /opt/app

COPY --from=dependencies /opt/app/node_modules ./node_modules
COPY --from=dependencies /opt/app/package.json ./
COPY --from=dependencies /opt/app/yarn.lock ./
COPY public ./public
COPY src ./src
COPY next.config.mjs .
COPY postcss.config.mjs .
COPY tailwind.config.ts .
COPY tsconfig.json .

RUN yarn run build

FROM node:20.16-slim AS runner

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en
ENV NODE_ENV=production

WORKDIR /opt/app

COPY --from=builder /opt/app/node_modules ./node_modules
COPY --from=builder /opt/app/package.json ./
COPY --from=builder /opt/app/yarn.lock ./
COPY --from=builder /opt/app/.next ./.next
COPY --from=builder /opt/app/public ./public
COPY --from=builder /opt/app/src ./src
COPY --from=builder /opt/app/next.config.mjs .
COPY --from=builder /opt/app/next-env.d.ts .
COPY --from=builder /opt/app/postcss.config.mjs .
COPY --from=builder /opt/app/tailwind.config.ts .
COPY --from=builder /opt/app/tsconfig.json .

RUN adduser -u 1500 --disabled-password --gecos "" docker_noroot
RUN chown -R docker_noroot:docker_noroot /opt/app
USER docker_noroot 

EXPOSE 3000

ENTRYPOINT ["yarn", "run", "start"]
CMD [""]
