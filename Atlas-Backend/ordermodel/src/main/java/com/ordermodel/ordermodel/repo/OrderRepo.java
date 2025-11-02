package com.ordermodel.ordermodel.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ordermodel.ordermodel.model.Order;

@Repository
public interface OrderRepo  extends JpaRepository<Order,UUID>{
    Order findByUserId(UUID userId);
}
