#!/bin/sh

if [ -z "$NGINX_CONF" ]; then
    echo "Error: NGINX_CONF is not set"
    exit 1
fi

GATEWAY_URL=${GATEWAY_URL:-"http://localhost:8085"}
sed -i "s|GATEWAY|$GATEWAY_URL|g" "$NGINX_CONF"
