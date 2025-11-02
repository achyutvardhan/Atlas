package com.ordermodel.ordermodel.feign;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.ordermodel.ordermodel.dto.orderRequest;

@FeignClient(name = "cartmodel" , path = "/cart")
public interface CartClient {
    @GetMapping("/{cartId}")
     public List<orderRequest> getAllCartItem(@RequestHeader("X-USER-ID") String userId,@PathVariable("cartId") UUID cartId);
}

