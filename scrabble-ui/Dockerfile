FROM node:20 AS build
WORKDIR /usr/local/app
COPY ./ /usr/local/app/

ARG KEYCLOAK_URL
ENV KEYCLOAK_URL $KEYCLOAK_URL

RUN npm install
RUN npm run build

FROM nginx:alpine
COPY --from=build /usr/local/app/dist/scrabble /usr/share/nginx/html

ARG GATEWAY_URL
ENV GATEWAY_URL $GATEWAY_URL

ENV NGINX_CONF="/etc/nginx/conf.d/nginx-server.conf"

RUN rm /etc/nginx/nginx.conf
RUN rm /etc/nginx/conf.d/default.conf

COPY src/nginx/nginx.conf /etc/nginx/nginx.conf
COPY src/nginx/nginx-server.conf /etc/nginx/conf.d/nginx-server.conf
COPY src/nginx/update-gateway-url.sh /etc/nginx/conf.d/update-gateway-url.sh

RUN chmod +x /etc/nginx/conf.d/update-gateway-url.sh
RUN /etc/nginx/conf.d/update-gateway-url.sh

EXPOSE 80
