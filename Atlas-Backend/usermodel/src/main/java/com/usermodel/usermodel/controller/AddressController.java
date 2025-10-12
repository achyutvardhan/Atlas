package com.usermodel.usermodel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.usermodel.usermodel.services.AddressService;

@RestController
public class AddressController {
    
    @Autowired
    private AddressService addressService;

    


}
