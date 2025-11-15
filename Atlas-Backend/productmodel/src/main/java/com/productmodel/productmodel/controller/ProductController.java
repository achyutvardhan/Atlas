package com.productmodel.productmodel.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productmodel.productmodel.dto.Productdto;
import com.productmodel.productmodel.dto.updateStockDto;
import com.productmodel.productmodel.services.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Productdto>> getProductsByCategory(@PathVariable("category") String category) {
        List<Productdto> dto = productService.getProductsByCategory(category);
        if (dto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dto);
    }

    
    @GetMapping("/{productId}")
    public ResponseEntity<Productdto> getProductById(@PathVariable("productId") UUID productId) {
        Productdto dto = productService.getProductById(productId);
        if (dto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dto);
    }

    
    @GetMapping("/all-products")
    public ResponseEntity<List<Productdto>> getAllProducts() {
        List<Productdto> dto = productService.getAllProducts();
        if (dto.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(dto);
    }
    
    @PostMapping("/addProduct")
    public ResponseEntity<Productdto> addProduct(@RequestBody Productdto prodto) {
        Productdto dto = productService.addProduct(prodto);
        if (dto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/udateStock/{productId}/{QuantityOrdered}")
    public ResponseEntity<updateStockDto> updateStock(@PathVariable("productId") UUID productId,
            @PathVariable("QuantityOrdered") int QuantityOrdered) {
        updateStockDto dto = productService.updateStock(productId, QuantityOrdered);
        if (dto == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(dto);
    }
}
