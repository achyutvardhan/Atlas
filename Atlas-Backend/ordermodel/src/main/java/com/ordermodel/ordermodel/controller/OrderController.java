package com.ordermodel.ordermodel.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordermodel.ordermodel.dto.orderResponse;
import com.ordermodel.ordermodel.services.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    //place order
    @GetMapping("/{userId}/place-order/{cartId}")
    public ResponseEntity<orderResponse> placeOrder(@PathVariable("userId") UUID userId ,@PathVariable("cartId") UUID cartId){
        orderResponse orderResponse = orderService.placeOrder(userId ,cartId);
        if(orderResponse==null) return ResponseEntity.status(400).build();
        return ResponseEntity.ok(orderResponse);
    }
    //get all orders of user by id
    @GetMapping("/get-all-order-by-id")
    public ResponseEntity<orderResponse> getAllOrderById(@RequestHeader(name = "X-USER-ID") String userId){
        orderResponse orderResponse = orderService.getAllOrderById(userId);
        if(orderResponse==null) return ResponseEntity.status(404).build();
        return ResponseEntity.ok(orderResponse);
    }

    //cancel order
    @GetMapping("/cancel-order/{id}")
    public ResponseEntity<orderResponse> cancelOrder(@RequestHeader(name = "X-USER-ID") String userId,@PathVariable UUID orderId){
        orderResponse orderResponse = orderService.cancelOrder(userId,orderId);
        if(orderResponse==null) return ResponseEntity.status(400).build();
        return ResponseEntity.ok(orderResponse);
    }

}
