package com.ordermodel.ordermodel.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class AddressDto {
    private UUID addressId;
    private String street;
    private String city;
    private String state;
    private int pincode;
}
