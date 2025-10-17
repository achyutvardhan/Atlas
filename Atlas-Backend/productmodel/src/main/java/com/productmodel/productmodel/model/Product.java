package com.productmodel.productmodel.model;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;
    private String productName;
    private int productQuantity;
    private boolean inStock;
    private int productRating;
    private Date productDoa;
    
    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Images> img;
    private int price;
    private String description;
    private String category;
}
