package com.cartmodel.cartmodel.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
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

    //  public String checkToken(String authHeader){
    //     String token = null;
    //     if (authHeader != null && authHeader.startsWith("Bearer ")) {
    //         token = authHeader.substring(7); 
    //     } else {
    //         return null;
    //     }
    //     return token;
    // }
    // private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    public List<cartResponse> getAllCartItem(String userId ,UUID cartId) {
        UUID userid = UUID.fromString(userId);
        Cart carts = cartRepo.findByUserId(userid); // userID from token
        if(carts == null || carts.getCartItems().isEmpty() || !carts.getCartId().equals(cartId) ) return null;

        List<cartResponse> cartResponses = carts.getCartItems().stream().map(
                cartItem -> {
                    cartResponse cartResponse = modelMapper.map(cartItem, cartResponse.class);
                    cartResponse.setCartId(carts.getCartId());
                    return cartResponse;
                }).collect(Collectors.toList());

        return cartResponses;
    }

    public boolean removeFromCart(String userId,UUID cartId, UUID cartItemId) {
         UUID userid = UUID.fromString(userId);
        // get userId from token and set to cartRequest
        Cart cart = cartRepo.findByUserId(userid); 
         if(cart == null || cart.getCartItems() == null || !cart.getCartId().equals(cartId)) return false;
        boolean removed = cart.getCartItems().removeIf(item -> item.getCartItemsId().equals(cartItemId));
        if(removed) cartRepo.save(cart);
        return removed;
    }

    public boolean clearCart(String userId,UUID cartId) {
        UUID userid = UUID.fromString(userId);
        Cart cart = cartRepo.findByUserId(cartId);
        if(cart.getUserId() != userid) return false;
        if(cart == null || cart.getCartItems() == null || !cart.getCartId().equals(cartId)) return false;
        if(cart.getCartItems().isEmpty()) return true;
        cart.getCartItems().clear();
        cartRepo.save(cart);
        return true;
    }

    public cartResponse addToCart(String userId,cartRequest cartRequest) {
        UUID userid = UUID.fromString(userId);
        Cart cart = cartRepo.findByUserId(userid);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userid); 
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
        resp.setCartId(saved.getCartId());
        return resp;
    }


    public orderResponse checkoutCart(String userId,UUID cartId)
    {
        UUID userid = UUID.fromString(userId);
        Cart cart = cartRepo.findByUserId(userid);
        if(cart == null || cart.getCartItems() == null || !cart.getCartId().equals(cartId)) return null;
        orderResponse dto = orderClient.placeOrder(userid ,cartId);
        if(dto == null) return null;
        cartRepo.delete(cart);
        return dto;
    }

}
