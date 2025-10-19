package com.gateway.apigateway.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.gateway.apigateway.util.JwtUtil;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // public JwtAuthenticationFilter ( JwtUtil jwtUtil){
    // this.jwtUtil = jwtUtil;
    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain filterChain) {
        final String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return filterChain.filter(exchange);
        }
        final String token = authHeader.substring(7);
        final UUID userId;

        try {
            userId = jwtUtil.extractUserId(token);
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            DataBuffer buffer = exchange.getResponse()
                    .bufferFactory()
                    .wrap("Token is invalid or expired".getBytes(StandardCharsets.UTF_8));
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }

        if (userId != null) {
            return ReactiveSecurityContextHolder.getContext()
                    .map(ctx -> ctx.getAuthentication())
                    .defaultIfEmpty(null)
                    .flatMap(existingAuth -> {
                        if (existingAuth == null) {
                            Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null,
                                    Collections.emptyList());
                            return filterChain.filter(exchange)
                                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                        } else {
                            return filterChain.filter(exchange);
                        }
                    });
        }

        return filterChain.filter(exchange);
    }
}
