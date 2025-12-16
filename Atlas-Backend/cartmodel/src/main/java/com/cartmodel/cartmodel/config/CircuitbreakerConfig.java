package com.cartmodel.cartmodel.config;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

@Configuration
public class CircuitbreakerConfig {
    
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(){
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .slidingWindowSize(10)
            .minimumNumberOfCalls(5)
            .failureRateThreshold(50.0f)
            .waitDurationInOpenState(Duration.ofSeconds(10))
            .permittedNumberOfCallsInHalfOpenState(3)
            .automaticTransitionFromOpenToHalfOpenEnabled(true)
            .slowCallDurationThreshold(Duration.ofSeconds(2))
            .slowCallRateThreshold(50.0f)
            .recordExceptions(
                FeignException.class,
                IOException.class,
                TimeoutException.class
            )
            .ignoreExceptions(IllegalArgumentException.class)
            .build();

            return CircuitBreakerRegistry.of(config);

    }

    @Bean
    public CircuitBreaker productServiceCircuitBreaker(CircuitBreakerRegistry registry){
        return registry.circuitBreaker("productmodel");
    }
}
