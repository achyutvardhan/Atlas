package com.gateway.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class apiGatewayConfig {

        @Autowired
        private RedisRateLimiter redisRateLimiter;

        // This tells Gateway to apply rate limit per client IP.
        @Bean
        public KeyResolver ipKeyResolver() {
                return exchange -> Mono.just(
                                exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
        }
        
        @Bean
        public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
                return builder.routes()
                                .route("usermodel", r -> r.path("/auth/**")
                                                .filters(f -> f.requestRateLimiter(c -> {
                                                        c.setRateLimiter(redisRateLimiter);
                                                        c.setKeyResolver(ipKeyResolver());
                                                        c.setDenyEmptyKey(false);
                                                }))
                                                .uri("http://localhost:8084"))
                                .route("productmodel", r -> r.path("/product/**")
                                                .filters(f -> f.circuitBreaker(c -> {
                                                        c.setName("productServiceCircuitBreaker");
                                                        c.setFallbackUri("/fallback/product");
                                                }))
                                                .uri("http://localhost:8083"))
                                .route("ordermodel", r -> r.path("/order/**")
                                                .uri("http://localhost:8082"))
                                .route("cartmodel", r -> r.path("/cart/**")
                                                .uri("http://localhost:8081"))
                                .route("sendemailservice", r -> r.path("/email/**")
                                                .uri("http://localhost:8087"))
                                .route("paymentgateway", r -> r.path("/payment/**")
                                                .uri("http://localhost:8086"))
                                .build();

        }

}
