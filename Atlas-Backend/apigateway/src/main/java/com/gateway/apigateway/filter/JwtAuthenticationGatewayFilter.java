package com.gateway.apigateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.UUID;

import org.springframework.http.HttpStatus;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.server.WebFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;

import com.gateway.apigateway.util.JwtUtil;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationGatewayFilter implements WebFilter {

    // private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationGatewayFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

 
    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange,@NonNull  WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Allow public endpoints
        if (path.startsWith("/auth/login") || path.startsWith("/auth/register")|| path.startsWith("/auth/admin/v1/register") || path.startsWith("/eureka/") || path.startsWith("/email/send-mail") || path.startsWith("/auth/verify-email") || path.startsWith("/actuator/") || path.startsWith("/payment/verify-payment/{userId}/{cartId}")) {
            return chain.filter(exchange);
        }


        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            DataBuffer buffer = exchange.getResponse()
                    .bufferFactory().wrap("Missing or invalid Authorization header".getBytes(StandardCharsets.UTF_8));
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }

        String token = authHeader.substring(7);

        try {
            if (jwtUtil.isTokenExpired(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                DataBuffer buffer = exchange.getResponse()
                        .bufferFactory().wrap("Token expired".getBytes(StandardCharsets.UTF_8));
                return exchange.getResponse().writeWith(Mono.just(buffer));
            }
         
            boolean role = jwtUtil.extractUserRole(token);
            if( !role && path.startsWith("/product/addProduct")||(role  && !path.startsWith("/product/addProduct"))){
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                DataBuffer buffer = exchange.getResponse()
                        .bufferFactory().wrap("Access Denied".getBytes(StandardCharsets.UTF_8));
                return exchange.getResponse().writeWith(Mono.just(buffer));
            }

            UUID userId = jwtUtil.extractUserId(token);

            Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-USER-ID", userId.toString())
                    .build();

            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

            return chain.filter(mutatedExchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            DataBuffer buffer = exchange.getResponse()
                    .bufferFactory().wrap(("Invalid token: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }
    }
}