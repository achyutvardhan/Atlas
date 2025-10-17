package com.ordermodel.ordermodel.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ordermodel.ordermodel.dto.ProductDto;

@FeignClient(name = "productmodel" , url = "http://localhost:8080/product")
public interface ProductClient {
    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable("id") UUID id);
}
