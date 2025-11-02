#!/usr/bin/env bash
set -euo pipefail

BASE_DIR="$(cd "$(dirname "$0")" && pwd)"

MVN_CMD="$BASE_DIR/mvnw"
if [ ! -x "$MVN_CMD" ]; then
  MVN_CMD="mvn"
fi

echo "Starting Eureka Server..."
cd "$BASE_DIR/eurekaserver" && "$MVN_CMD"  spring-boot:run &
sleep 10
echo "Starting Api Gateway..."
cd "$BASE_DIR/apigateway" && "$MVN_CMD"  spring-boot:run &
sleep 10
echo "Starting User Service..."
cd "$BASE_DIR/usermodel" && "$MVN_CMD" spring-boot:run &
sleep 10
echo "Starting Product Service..."
cd "$BASE_DIR/productmodel" && "$MVN_CMD"  spring-boot:run &
sleep 10
echo "Starting Order Service..."
cd "$BASE_DIR/ordermodel" && "$MVN_CMD"  spring-boot:run &
sleep 10
echo "Starting Cart Service..."
cd "$BASE_DIR/cartmodel" && "$MVN_CMD"  spring-boot:run &

wait