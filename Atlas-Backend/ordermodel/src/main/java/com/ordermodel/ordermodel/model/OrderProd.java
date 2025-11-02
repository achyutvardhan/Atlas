package com.ordermodel.ordermodel.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class OrderProd {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ordProdId;
    private String productName;
    private int QuantityAdded;
    private int price;
    private String description;
    private String category;
    private boolean inStock;
}
