package com.ordermodel.ordermodel.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class orderRequest {
    private UUID productId;
    private UUID cartItemsId;
    private int QuantityAdded;
}
