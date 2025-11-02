package com.ordermodel.ordermodel.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ordermodel.ordermodel.dto.AddressDto;

@FeignClient(name = "addressmodel" , url = "/api")
public interface AddressClient {
    @GetMapping("/address/{id}")
    public AddressDto getAddressDtoById(@PathVariable("id") UUID id);
}
