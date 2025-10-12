package com.ordermodel.ordermodel.model;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "orders")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    private UUID usersId;

    @OneToMany(mappedBy = "order")
    private java.util.List<OrderProd> orderProds;

    private Date orderedDate;
    private boolean status;
    private UUID addressId;

}
