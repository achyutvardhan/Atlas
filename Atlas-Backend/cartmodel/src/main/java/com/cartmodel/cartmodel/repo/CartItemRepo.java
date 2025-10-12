package com.cartmodel.cartmodel.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cartmodel.cartmodel.model.CartItems;

@Repository
public interface CartItemRepo extends JpaRepository<CartItems,UUID> {
    
}
