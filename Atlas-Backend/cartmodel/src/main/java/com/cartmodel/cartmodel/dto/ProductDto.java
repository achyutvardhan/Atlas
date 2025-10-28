package com.cartmodel.cartmodel.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class ProductDto {
    private UUID productId;
    private String productName;
    private int productQuantity;
    private boolean inStock;
    private int price;
    private String description;
    private String category;
}
