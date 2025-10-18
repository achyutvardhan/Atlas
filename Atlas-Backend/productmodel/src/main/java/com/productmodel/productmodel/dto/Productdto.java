package com.productmodel.productmodel.dto;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import com.productmodel.productmodel.model.Images;

import lombok.Data;

@Data
public class Productdto {
     private UUID productId;
    private String productName;
    private int productQuantity;
    private boolean inStock;
    private int productRating;
    private Date productDoa;
    private int price;
    private String description;
    private String category;
    private List<Images> img;
}
