package com.ordermodel.ordermodel.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ordermodel.ordermodel.dto.ProductDto;
import com.ordermodel.ordermodel.dto.orderRequest;
import com.ordermodel.ordermodel.dto.orderResponse;
import com.ordermodel.ordermodel.dto.updateStockDto;
import com.ordermodel.ordermodel.feign.CartClient;
import com.ordermodel.ordermodel.feign.ProductClient;
import com.ordermodel.ordermodel.model.Order;
import com.ordermodel.ordermodel.model.OrderProd;
import com.ordermodel.ordermodel.repo.OrderRepo;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductClient ProductClient;

    @Autowired
    private CartClient cartClient;

    @Autowired
    private ModelMapper modelMapper;

    public String checkToken(String authHeader) {
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else {
            return null;
        }
        return token;
    }

    public orderResponse placeOrder(UUID userId, UUID cartId) {
        List<orderRequest> orderRequests = cartClient.getAllCartItem(userId.toString(), cartId);
        List<OrderProd> savedOrders = new ArrayList<>();
        orderRequests.forEach(product -> {
            ProductDto productdto = ProductClient.getProductById(product.getProductId());
            OrderProd orderProd = modelMapper.map(productdto, OrderProd.class);
            orderProd.setQuantityAdded(product.getQuantityAdded());
            // update product quantity in product service
            updateStockDto dto = ProductClient.updateStock(product.getProductId(), product.getQuantityAdded());
            if (dto == null || !dto.isUpdated()) {
                String message = dto == null ? "Product Service not available" : dto.getMessage();
                throw new RuntimeException(message);
            } else
                savedOrders.add(orderProd);
        });
        Order newOrder = new Order();
        newOrder.setOrderProds(savedOrders);
        newOrder.setOrderedDate(new Date(System.currentTimeMillis()));
        newOrder.setStatus(true);
        newOrder.setUserId(userId);
        orderRepo.save(newOrder);
        orderResponse orderResponses = modelMapper.map(newOrder, orderResponse.class);
        return orderResponses;
    }

    public orderResponse getAllOrderById(String userId) {
        UUID userid = UUID.fromString(userId);

        Order order = orderRepo.findByUserId(userid); // use userId to get all orders
        if (order == null)
            return null;
        orderResponse orderResponse = modelMapper.map(order, orderResponse.class);
        return orderResponse;
    }

    public orderResponse cancelOrder(String userId, UUID orderId) {
        UUID userid = UUID.fromString(userId);
        Order order = orderRepo.findById(orderId).orElse(null);
        if (order == null || order.getUserId() != userid)
            return null; // check if order belongs to user
        order.setStatus(false);
        order.setCancelled(true);
        orderRepo.save(order);
        orderResponse orderResponse = modelMapper.map(order, orderResponse.class);
        return orderResponse;

    }

}
