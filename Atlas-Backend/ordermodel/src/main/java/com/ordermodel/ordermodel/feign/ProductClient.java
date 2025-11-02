package com.ordermodel.ordermodel.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ordermodel.ordermodel.dto.ProductDto;
import com.ordermodel.ordermodel.dto.updateStockDto;

@FeignClient(name = "productmodel" , path = "/product")
public interface ProductClient {
    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable("productId") UUID productId);

    @GetMapping("/udateStock/{productId}/{QuantityOrdered}")
    public updateStockDto updateStock(@PathVariable("productId") UUID productId,
            @PathVariable("QuantityOrdered") int QuantityOrdered);
}
