package com.ordermodel.ordermodel.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.ordermodel.ordermodel.dto.ProductDto;
import com.ordermodel.ordermodel.dto.orderRequest;
import com.ordermodel.ordermodel.dto.orderResponse;
import com.ordermodel.ordermodel.feign.CartClient;
import com.ordermodel.ordermodel.feign.ProductClient;
import com.ordermodel.ordermodel.model.Order;
import com.ordermodel.ordermodel.model.OrderProd;
import com.ordermodel.ordermodel.repo.OrderRepo;

public class OrderService {
    
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductClient ProductClient;

    @Autowired
    private CartClient cartClient;

    @Autowired
    private ModelMapper modelMapper;

    public orderResponse placeOrder(UUID cartId) {
        List<orderRequest> orderRequests = cartClient.getAllCartItem(cartId);
         List<OrderProd> savedOrders = new ArrayList<>();
        orderRequests.forEach(product -> {
            ProductDto productdto = ProductClient.getProductById(product.getProductId());
            OrderProd orderProd = modelMapper.map(productdto, OrderProd.class);
            orderProd.setQuantityAdded(product.getQuantityAdded());
            savedOrders.add(orderProd);
        });
        Order newOrder = new Order();
        newOrder.setOrderProds(savedOrders);
        newOrder.setOrderedDate(new Date(System.currentTimeMillis()));
        newOrder.setStatus(true);
        orderRepo.save(newOrder);
        orderResponse orderResponses = modelMapper.map(newOrder, orderResponse.class);
        return orderResponses;
    }

    public orderResponse getAllOrderById(UUID orderId){
        Order order = orderRepo.findById(orderId).orElse(null);
        if(order == null) return null;
        orderResponse orderResponse = modelMapper.map(order, orderResponse.class);
        return orderResponse;
    }

    public orderResponse cancelOrder(UUID orderId){
        Order order = orderRepo.findById(orderId).orElse(null);
        if (order == null)return null;
        order.setStatus(false);
        order.setCancelled(true);
        orderRepo.save(order);
        orderResponse orderResponse = modelMapper.map(order, orderResponse.class);
        return orderResponse;
        
    }


}
