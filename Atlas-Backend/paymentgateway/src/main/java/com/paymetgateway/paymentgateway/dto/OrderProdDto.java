package com.paymetgateway.paymentgateway.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class OrderProdDto {
    private UUID ordProdId;
    private int quantity;
    private int priceAtPurchase;
}
