package com.cartmodel.cartmodel.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.cartmodel.cartmodel.dto.Userdto;

@FeignClient(name = "usermodel" , url = "http://localhost:8081/api")
public interface UserClient {
    @GetMapping("/{id}")
    public Userdto getUserById(UUID id);
}
