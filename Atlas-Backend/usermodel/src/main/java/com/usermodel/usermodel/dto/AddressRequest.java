package com.usermodel.usermodel.dto;

import lombok.Data;

@Data
public class AddressRequest {
    private String street;
    private String city;
    private String state;
    private int pincode;
}
