package com.ordermodel.ordermodel.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordermodel.ordermodel.dto.orderRequest;
import com.ordermodel.ordermodel.dto.orderResponse;
import com.ordermodel.ordermodel.services.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    //place order
    @PostMapping("/place-order")
    public ResponseEntity<orderResponse> placeOrder(@RequestBody UUID cartId){
        orderResponse orderResponse = orderService.placeOrder(cartId);
        if(orderResponse==null) return ResponseEntity.status(400).build();
        return ResponseEntity.ok(orderResponse);
    }
    //get all orders of user by id
    @GetMapping("/get-all-order-by-id/{id}")
    public ResponseEntity<orderResponse> getAllOrderById(@PathVariable UUID orderId){
        orderResponse orderResponse = orderService.getAllOrderById(orderId);
        if(orderResponse==null) return ResponseEntity.status(404).build();
        return ResponseEntity.ok(orderResponse);
    }

    //cancel order
    @GetMapping("/cancel-order/{id}")
    public ResponseEntity<orderResponse> cancelOrder(@PathVariable UUID orderId){
        orderResponse orderResponse = orderService.cancelOrder(orderId);
        if(orderResponse==null) return ResponseEntity.status(400).build();
        return ResponseEntity.ok(orderResponse);
    }

}
