package com.ordermodel.ordermodel.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.ordermodel.ordermodel.dto.AddressDto;

@FeignClient(name = "addressmodel" , url = "http://localhost:8080/api")
public interface AddressClient {
    @GetMapping("/address/{id}")
    public AddressDto getAddressDtoById(UUID id);
}
