package com.usermodel.usermodel.dto;


import lombok.Data;

@Data
public class AddressResponse {
    private String street;
    private String city;
    private String state;
    private int pincode;
}
