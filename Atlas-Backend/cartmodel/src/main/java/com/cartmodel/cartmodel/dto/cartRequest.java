package com.cartmodel.cartmodel.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class cartRequest {
    private UUID cartId;
    private int QuantityAdded;
    private UUID productId;
    private String productName;
}
