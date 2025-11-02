package com.ordermodel.ordermodel.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class OrderProdDto {
    private UUID ordProdId;
    private String productName;
    private int QuantityAdded;
    private int price;
    private String description;
    private String category;
}
