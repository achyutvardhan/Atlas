package com.ordermodel.ordermodel.dto;

import java.sql.Date;
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
