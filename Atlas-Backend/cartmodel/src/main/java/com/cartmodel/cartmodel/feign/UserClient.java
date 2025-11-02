package com.cartmodel.cartmodel.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cartmodel.cartmodel.dto.Userdto;

@FeignClient(name = "usermodel" , url = "/auth")
public interface UserClient {
    @GetMapping("/{id}")
    public Userdto getUserById(@PathVariable("id") UUID id);
}
