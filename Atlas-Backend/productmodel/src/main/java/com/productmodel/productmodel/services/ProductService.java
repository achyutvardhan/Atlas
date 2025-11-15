package com.productmodel.productmodel.services;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.productmodel.productmodel.dto.Productdto;
import com.productmodel.productmodel.dto.updateStockDto;
import com.productmodel.productmodel.model.Product;
import com.productmodel.productmodel.repo.ProductRepository;

@Service
@CacheConfig(cacheNames = "products")
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @CachePut(key = "#category")
    public List<Productdto> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        return products.stream()
                .map(product -> modelMapper.map(product, Productdto.class))
                .toList();
    }

    @CachePut(key = "#id")
    public Productdto getProductById(UUID id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null)
            return null;
        Productdto productdto = modelMapper.map(product, Productdto.class);
        return productdto;
    }
    @Cacheable(key = "'all-products'")
    public List<Productdto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty())
            return Collections.emptyList();
        ;
        return products.stream().map(p -> modelMapper.map(p, Productdto.class)).toList();
    }
    @Caching(
        evict = {@CacheEvict(key = "'all-products'", allEntries = true),},
        put = {@CachePut(key = "'all-products'")}
    )
    public Productdto addProduct(Productdto productdto) {
        Product product = modelMapper.map(productdto, Product.class);
        if (product == null)
            return null;
        product.setProductDoa(new Date(System.currentTimeMillis()));
        Product saved = productRepository.save(product);
        return modelMapper.map(saved, Productdto.class);
    }
    
    @Caching(
        evict = {@CacheEvict(key = "'all-products'", allEntries = true),},
        put = {@CachePut(key = "'all-products'")}
    )
    public updateStockDto updateStock(UUID productId, int quantityOrdered) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getProductQuantity() < quantityOrdered) {
            updateStockDto dto = new updateStockDto();
            dto.setUpdated(false);
            dto.setMessage("Insufficient stock");
            return dto;
        }

        product.setProductQuantity(product.getProductQuantity() - quantityOrdered);
        productRepository.save(product);

        updateStockDto dto = new updateStockDto();
        dto.setUpdated(true);
        dto.setMessage("Stock updated successfully");
        return dto;
    }
}
