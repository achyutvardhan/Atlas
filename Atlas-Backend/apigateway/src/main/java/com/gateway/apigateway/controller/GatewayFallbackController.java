package com.gateway.apigateway.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class GatewayFallbackController {
    @RequestMapping("/product")
    public ResponseEntity<Map<String, Object>> getProductFallback() {
        return createFallbackResponse("Product Service");
    }

    @RequestMapping("/cart")
    public ResponseEntity<Map<String, Object>> getCartFallback() {
        return createFallbackResponse("Cart Service");
    }

    @RequestMapping("/order")
    public ResponseEntity<Map<String, Object>> getOrderFallback() {
        return createFallbackResponse("Order Service");
    }

    @RequestMapping("/payment")
    public ResponseEntity<Map<String, Object>> getPaymentFallback() {
        return createFallbackResponse("Payment Service");
    }

    @RequestMapping("/seller")
    public ResponseEntity<Map<String, Object>> getSellerFallback() {
        return createFallbackResponse("Seller Service");
    }

    @RequestMapping("/email")
    public ResponseEntity<Map<String, Object>> getEmailFallback() {
        return createFallbackResponse("Email Service");
    }

    private ResponseEntity<Map<String, Object>> createFallbackResponse(String serviceName) {
        System.out.println("Circuit breaker activated - " + serviceName + " fallback triggered");
        
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("error", "Service Unavailable");
        response.put("message", serviceName + " is temporarily unavailable. Please try again later.");
        response.put("service", serviceName);
        
        return ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(response);
    }

}