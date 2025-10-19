package com.cartmodel.cartmodel.repo;

import java.util.UUID;
import java.util.Locale.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cartmodel.cartmodel.model.Cart;

@Repository
public interface CartRepo extends JpaRepository<Cart,UUID> {
    Cart findByUserId(UUID userId);
}
