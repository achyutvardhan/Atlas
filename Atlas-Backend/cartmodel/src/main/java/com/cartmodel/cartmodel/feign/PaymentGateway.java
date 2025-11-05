package com.cartmodel.cartmodel.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cartmodel.cartmodel.dto.CreateOrderDto;

@FeignClient(name = "paymentgateway" , path = "/payment")
public interface PaymentGateway {
    @PostMapping("/create-order")
    public String createOrder(@RequestBody CreateOrderDto createOrder);
}
