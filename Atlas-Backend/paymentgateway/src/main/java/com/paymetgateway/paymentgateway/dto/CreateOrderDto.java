package com.paymetgateway.paymentgateway.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class CreateOrderDto {
    private int amount;
    private String currency;
    private UUID receiptId;
    private UUID userId;
    private UUID cartId;
    private String shippingAddress;
    private String email;
    private String phoneNumber;
}