package com.ordermodel.ordermodel.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class orderRequest {
    private int QuantityAdded;
    private UUID productId;
    private UUID cartId;
    private String productName;
    private UUID cartItemsId;
}
