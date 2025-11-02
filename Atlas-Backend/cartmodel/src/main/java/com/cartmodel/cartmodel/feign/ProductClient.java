package com.cartmodel.cartmodel.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cartmodel.cartmodel.dto.ProductDto;

@FeignClient(name = "productmodel" , path = "/product")
public interface ProductClient {
    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable("productId") UUID productId);
}
