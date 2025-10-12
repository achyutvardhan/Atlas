package com.usermodel.usermodel.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class AddressResp {
    private UUID addressId;
    private String street;
    private String city;
    private String state;
    private int pincode;
}
