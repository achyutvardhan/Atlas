package com.paymetgateway.paymentgateway.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.paymetgateway.paymentgateway.dto.orderResponse;

@FeignClient(name = "ordermodel" , path = "/order")
public interface OrderClient {
    @GetMapping("/{userId}/place-order/{cartId}")
    public orderResponse placeOrder(@PathVariable("userId") UUID userId , @PathVariable("cartId") UUID cartId);
}
