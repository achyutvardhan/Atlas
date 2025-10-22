package com.gateway.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class apiGatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("usermodel", r -> r.path("/auth/**")
                        .uri("http://localhost:8084"))
                .route("productmodel", r -> r.path("/product/**")
                        .uri("http://localhost:8083"))
                .route("ordermodel", r -> r.path("/order/**")
                        .uri("http://localhost:8082"))
                .route("cartmodel", r -> r.path("/cart/**")
                        .uri("http://localhost:8081"))
                .build();

    }
}
