package com.cartmodel.cartmodel.dto;

import java.util.UUID;


import lombok.Data;

@Data
public class cartResponse {
    private UUID cartItemsId;
    private int QuantityAdded;
    private UUID productId;
}
