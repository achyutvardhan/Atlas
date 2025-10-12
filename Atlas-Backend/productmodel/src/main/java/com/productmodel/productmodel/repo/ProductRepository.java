package com.productmodel.productmodel.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productmodel.productmodel.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    
}
