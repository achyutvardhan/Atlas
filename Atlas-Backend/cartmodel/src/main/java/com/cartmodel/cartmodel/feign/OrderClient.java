package com.cartmodel.cartmodel.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.cartmodel.cartmodel.dto.orderResponse;

@FeignClient(name = "ordermodel", url = "/order")
public interface OrderClient {
    @PostMapping("/place-order")
    public orderResponse placeOrder(@RequestBody UUID cartId);
}
