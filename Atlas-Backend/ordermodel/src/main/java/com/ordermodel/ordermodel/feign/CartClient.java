package com.ordermodel.ordermodel.feign;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ordermodel.ordermodel.dto.orderRequest;

@FeignClient(name = "cartmodel" , url = "/cart")
public interface CartClient {
    @GetMapping("/{cartId}")
     public List<orderRequest> getAllCartItem(@RequestBody UUID cartId);
}
