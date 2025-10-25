package com.cartmodel.cartmodel.dto;

import java.util.UUID;


import lombok.Data;

@Data
public class cartResponse {
    private int QuantityAdded;
    private UUID productId;
    private UUID cartId;
    private String productName;
    private UUID cartItemsId;
}
