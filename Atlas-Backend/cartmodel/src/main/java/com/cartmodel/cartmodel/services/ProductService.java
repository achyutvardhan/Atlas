package com.cartmodel.cartmodel.services;

import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cartmodel.cartmodel.dto.ProductDto;
import com.cartmodel.cartmodel.feign.ProductClient;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.vavr.control.Try;

@Service
public class ProductService {
    
    @Autowired
    private ProductClient productClient;

    @Autowired
    @Qualifier("productServiceCircuitBreaker")
    private CircuitBreaker productServiceCircuitBreaker;

    public ProductDto getProductById(UUID productId){
        Supplier<ProductDto> decoratedSupplier = CircuitBreaker.decorateSupplier(productServiceCircuitBreaker, ()-> productClient.getProductById(productId));

        return Try.ofSupplier(decoratedSupplier)
                .recover(throwable -> getFallbackProduct(productId))
                .get();
    }

    private ProductDto getFallbackProduct(UUID productId) {
        ProductDto fallbackUser = new ProductDto();
        fallbackUser.setProductId(productId);
        fallbackUser.setProductName("Default Product");
        fallbackUser.setProductQuantity(0);
        fallbackUser.setInStock(false);
        fallbackUser.setPrice(0);
        fallbackUser.setDescription("This is a fallback product description.");
        fallbackUser.setCategory("N/A");
        return fallbackUser;
    }
}
