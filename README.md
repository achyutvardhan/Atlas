# Atlas

**Atlas** is a microservices-based ecommerce platform blueprint built with Spring Boot and an optional JavaScript frontend.

- Backend: Spring Boot (Java 21), Spring Cloud (Eureka, Gateway, Config, Feign), Resilience4j, Zipkin, Redis, JWT/OAuth2.
- Deployment pattern: modular microservices with central service discovery and API gateway.

## Status

- ✓ Project structure analyzed
- ✓ `Atlas-Backend` multi-module Maven parent + 9 microservices
- ✓ `configserver` and `eurekaserver` present for configuration/service discovery
- ✓ `start-service.sh` container for local orchestration

## Architecture

1. **Eureka Server** (`Atlas-Backend/eurekaserver`) - service registry.
2. **API Gateway** (`Atlas-Backend/apigateway`) - routing, security, metrics, circuit breaker.
3. **Domain services**:
   - `usermodel` (user account, auth)
   - `productmodel` (catalog)
   - `ordermodel` (orders)
   - `cartmodel` (shopping cart)
   - `paymentgateway` (payment orchestration)
   - `sendemailservice` (email notifications)
   - `sellermodel` (seller workflows)
4. **Config Server** (`configserver`) - centralized property configuration.

## Technology Stack

- Java 21
- Spring Boot 3.5.x (each microservice module)
- Spring Cloud 2025 (Eureka, Gateway, Config, Feign, Circuit Breaker)
- Spring Cloud Gateway (`Atlas-Backend/apigateway`)
- Netflix Eureka (`Atlas-Backend/eurekaserver`)
- Spring Cloud Config (`configserver`)
- Spring Security (JWT/OAuth2) (`Atlas-Backend/apigateway`, `usermodel`)
- Resilience4j and Micrometer/Zipkin tracing (`Atlas-Backend/apigateway`)
- Redis (Reactive caching/session; gateway rate limiting; `productmodel` cache) (`Atlas-Backend/apigateway`, `Atlas-Backend/productmodel`)
- Kafka event stream (`usermodel`, `sendemailservice`)
- Résilience: Resilience4j circuit breaker + retries (`Atlas-Backend/cartmodel`, `Atlas-Backend/apigateway`)
- GitHub config backend for Spring Cloud Config (`configserver/src/main/resources/application.properties`)
- Lombok (all Java modules)
- Maven (build, dependency management)
- Bash script startup (`Atlas-Backend/start-service.sh`)
- Docker Compose placeholder `docker-compose.yml` in service folders

### Module technology mapping

- `Atlas-Backend/eurekaserver`: Eureka registry service
- `Atlas-Backend/apigateway`: API gateway, routing, security, observability
- `Atlas-Backend/usermodel`: user service, auth and profile management
- `Atlas-Backend/productmodel`: product catalog service
- `Atlas-Backend/ordermodel`: order processing service
- `Atlas-Backend/cartmodel`: cart session/service
- `Atlas-Backend/paymentgateway`: payment orchestration
- `Atlas-Backend/sendemailservice`: email notifications
- `Atlas-Backend/sellermodel`: seller accounts and listing management
- `configserver`: Spring Cloud Config repository and config server

## Prerequisites

- Java 21 (OpenJDK 21+)
- Maven 3.9+ (`mvn`), or use bundled wrapper scripts (`mvnw`, `mvnw.cmd`)
- git
- Optional: Docker 20+, Docker Compose for containerized deployments

## Clone

```bash
git clone https://github.com/achyutvardhan/Atlas.git
cd Atlas
```

## Build

### Build all backend modules (root of repo)

```bash
cd Atlas-Backend
./mvnw clean install -DskipTests
```

or if `mvnw` is not executable:

```bash
mvn clean install -DskipTests
```

## Run (local dev)

### 1) Quick local startup script

From `Atlas-Backend`:

```bash
./start-service.sh
```

This starts services in sequence (Eureka, Gateway, User, Product, Order, Cart) with sleeps.

### 2) Manual service launch order (recommended)

1. `cd Atlas-Backend/eurekaserver && ./mvnw spring-boot:run`
2. `cd Atlas-Backend/apigateway && ./mvnw spring-boot:run`
3. `cd Atlas-Backend/usermodel && ./mvnw spring-boot:run`
4. `cd Atlas-Backend/productmodel && ./mvnw spring-boot:run`
5. `cd Atlas-Backend/ordermodel && ./mvnw spring-boot:run`
6. `cd Atlas-Backend/cartmodel && ./mvnw spring-boot:run`
7. `cd Atlas-Backend/paymentgateway && ./mvnw spring-boot:run`
8. `cd Atlas-Backend/sendemailservice && ./mvnw spring-boot:run`
9. `cd Atlas-Backend/sellermodel && ./mvnw spring-boot:run`

> Tip: for each service, logs show registration in Eureka and gateway routes.

## Test

Run tests for all modules:

```bash
cd Atlas-Backend
./mvnw test
```

## API

- Gateway likely exposed at `http://localhost:8080` (verify via `application.yml` in `apigateway/src/main/resources`)
- Eureka dashboard: `http://localhost:8761`
- Services auto-register and route via Eureka IDs (use gateway route config)

## Docker

No active Docker Compose content in service folders; individual module `docker-compose.yml` files are present but empty in most modules.

## Useful commands

- `./mvnw -pl <module> spring-boot:run` to run one module from parent.
- `./mvnw -pl <module> test` to run unit tests in one module.
- `./mvnw dependency:tree` for troubleshooting.

## Recommended inspection

- `Atlas-Backend/pom.xml` (parent module, Java+Spring versions)
- `Atlas-Backend/eurekaserver/src/main/resources/application.yml` and `configserver/src/main/resources/application.yml` for endpoint setups.
- `Atlas-Backend/apigateway/src/main/resources/application.yml` for gateway route definitions.

## Enhancements

1. Implement Docker Compose orchestration with one root file including Eureka + Config + Gateway + services.
2. Add a `frontend` directory and instructions for React/Vite integration if not already included.
3. Add CI (GitHub Actions) to run `./mvnw test` and Docker build.

## Troubleshooting

- `java.net.ConnectException` on service startup: start Eureka first.
- Port conflict: adjust `server.port` in each service.
- Missing configuration property: verify config source for `configserver` or local `application.yml`.

## License

MIT-style / your choice.
