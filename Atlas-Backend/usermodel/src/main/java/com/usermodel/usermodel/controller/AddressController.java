package com.usermodel.usermodel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usermodel.usermodel.dto.AddressRequest;
import com.usermodel.usermodel.dto.AddressResponse;
import com.usermodel.usermodel.services.userService;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    
    @Autowired
    private userService userService;

    @PostMapping("/add-user-address")
    public ResponseEntity<AddressResponse> addUserAddress(@RequestHeader(name = "Authorization") String authHeader, @RequestBody AddressRequest adr)
    {
        AddressResponse adrep = userService.saveAddress(authHeader, adr);
        if(adrep == null)return ResponseEntity.status(403).build();
        return ResponseEntity.ok().body(adrep);
    }

    @GetMapping("/get-All-user-address")
    public ResponseEntity<List<AddressResponse>> getAllUserAddress(@RequestHeader(name = "Authorization") String authHeader)
    {
        List<AddressResponse> adrep = userService.getAllUserAddress(authHeader);
        return ResponseEntity.ok().body(adrep);
    }


}
