package com.cartmodel.cartmodel.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cartmodel.cartmodel.dto.cartRequest;
import com.cartmodel.cartmodel.dto.cartResponse;
import com.cartmodel.cartmodel.dto.orderResponse;
import com.cartmodel.cartmodel.feign.OrderClient;
import com.cartmodel.cartmodel.model.Cart;
import com.cartmodel.cartmodel.model.CartItems;
import com.cartmodel.cartmodel.repo.CartRepo;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private ModelMapper modelMapper;

     public String checkToken(String authHeader){
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); 
        } else {
            return null;
        }
        return token;
    }

    public List<cartResponse> getAllCartItem(String authHeader) {
        String token = checkToken(authHeader);
        if(token == null) return null;
        // get userId from token and set to cartRequest
        Cart carts = cartRepo.findByUserId().orElse(null); // userID from token
        if(carts == null || carts.getCartItems() == null) return new ArrayList<>();
        List<cartResponse> cartResponses = carts.getCartItems().stream().map(
                cartItem -> {
                    cartResponse cartResponse = modelMapper.map(cartItem, cartResponse.class);
                    return cartResponse;
                }).collect(Collectors.toList());

        return cartResponses;
    }

    public boolean removeFromCart(String authHeader,UUID cartId, UUID cartItemId) {
        String token = checkToken(authHeader);
        if(token == null) return false;
        // get userId from token and set to cartRequest
        Cart cart = cartRepo.findById(cartId).orElse(null);
         if(cart == null || cart.getCartItems() == null) return false;
        boolean removed = cart.getCartItems().removeIf(item -> item.getCartItemsId().equals(cartItemId));
        if(removed) cartRepo.save(cart);
        return removed;
    }

    public boolean clearCart(String authHeader,UUID cartId) {
        String token = checkToken(authHeader);
        if(token == null) return false;
        // get userId from token 
        Cart cart = cartRepo.findById(cartId).orElse(new Cart());
        if(cart.getUsersId() != /* userID from token */) return false;
        if(cart == null || cart.getCartItems() == null) return false;
        if(cart.getCartItems().isEmpty()) return true;
        cart.getCartItems().clear();
        cartRepo.save(cart);
        return true;
    }

    public cartResponse addToCart(String authHeader,cartRequest cartRequest) {
        String token = checkToken(authHeader);
        if(token == null) return null;
        // get userId from token and set to cartRequest
        Cart cart = cartRepo.findById(cartRequest.getCartId()).orElse(null);
        if (cart == null) {
            cart = new Cart();
            cart.setUsersId(); // userID from token
            cart.setCartItems(new ArrayList<>());
        } else if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }
        CartItems existing = cart.getCartItems().stream()
                .filter(ci -> ci.getProductId().equals(cartRequest.getProductId()))
                .findFirst()
                .orElse(null);
        if (existing != null) {
            existing.setQuantityAdded(existing.getQuantityAdded() + cartRequest.getQuantityAdded());
        } else {
            CartItems cartItem = modelMapper.map(cartRequest, CartItems.class);
            cartItem.setCartItemsId(null); 
            cart.getCartItems().add(cartItem);
        }

        Cart saved = cartRepo.save(cart);

        CartItems returnedItem = saved.getCartItems().stream()
                .filter(ci -> ci.getProductId().equals(cartRequest.getProductId()))
                .findFirst()
                .orElse(null);

        if (returnedItem == null)
            return null;
        cartResponse resp = modelMapper.map(returnedItem, cartResponse.class);
        return resp;
    }


    public orderResponse checkoutCart(String authHeader,UUID cartId)
    {
        String token = checkToken(authHeader);
        if(token == null) return null;
        // get userId from token
        orderResponse dto = orderClient.placeOrder(cartId);
        if(dto == null) return null;
        Cart cart = cartRepo.findById(cartId).orElse(null);
        cartRepo.delete(cart);
        return dto;
    }

}
