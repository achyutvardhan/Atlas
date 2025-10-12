package com.ordermodel.ordermodel.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ordermodel.ordermodel.model.OrderProd;

@Repository
public interface OrderProdRepo extends JpaRepository<OrderProd,UUID> {
    
}
