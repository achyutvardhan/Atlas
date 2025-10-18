package com.productmodel.productmodel.services;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productmodel.productmodel.dto.Productdto;
import com.productmodel.productmodel.model.Product;
import com.productmodel.productmodel.repo.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<Productdto> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        return products.stream()
                .map(product -> modelMapper.map(product, Productdto.class))
                .toList();
    }

    public Productdto getProductById(UUID id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null)
            return null;
        Productdto productdto = modelMapper.map(product, Productdto.class);
        return productdto;
    }

    public List<Productdto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty())
            return  Collections.emptyList();;
        return products.stream().map(p -> modelMapper.map(p, Productdto.class)).toList();
    }

    public Productdto addProduct(Productdto productdto) {
        Product product = modelMapper.map(productdto, Product.class);
        if (product == null)
            return null;
            Product saved = productRepository.save(product);
        return modelMapper.map(saved, Productdto.class);
    }
}
