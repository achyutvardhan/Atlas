package com.productmodel.productmodel.dto;

import java.util.List;

import com.productmodel.productmodel.model.Images;

import lombok.Data;

@Data
public class Productdto {
    private String productName;
    private int productQuantity;
    private boolean inStock;
    private int productRating;
    private int price;
    private String description;
    private String category;
    private List<Images> img;
}
