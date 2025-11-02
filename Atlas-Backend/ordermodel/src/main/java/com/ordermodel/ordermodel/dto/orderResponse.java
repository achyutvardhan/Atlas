package com.ordermodel.ordermodel.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class orderResponse {
    private UUID orderId;
    private List<OrderProdDto> orderProds;
    private boolean status;
    private boolean isCancelled;
    private UUID addressId;
}
