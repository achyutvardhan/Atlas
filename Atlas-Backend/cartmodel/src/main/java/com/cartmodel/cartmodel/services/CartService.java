package com.cartmodel.cartmodel.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cartmodel.cartmodel.dto.Userdto;
import com.cartmodel.cartmodel.dto.cartRequest;
import com.cartmodel.cartmodel.dto.cartResponse;
import com.cartmodel.cartmodel.feign.UserClient;
import com.cartmodel.cartmodel.model.Cart;
import com.cartmodel.cartmodel.model.CartItems;
import com.cartmodel.cartmodel.repo.CartRepo;

@Service
public class CartService {
    @Autowired
    private UserClient userClient;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<cartResponse> getAllCartItem(UUID id)
    {
        Cart carts = cartRepo.findById(id).orElse(null);
        List<cartResponse> cartResponses = carts.getCartItems().stream().map(
            cartItem -> {
                cartResponse cartResponse = modelMapper.map(cartItem, cartResponse.class);
                return cartResponse;
            }
        ).collect(Collectors.toList());

        return cartResponses;
    }


    public boolean removeFromCart(UUID cartId, UUID cartItemId){
        Cart cart = cartRepo.findById(cartId).orElse(new Cart());
        cart.getCartItems().removeIf(item -> item.getCartItemsId().equals(cartItemId));
        cartRepo.save(cart);
        return true;
    }

    public boolean clearCart(UUID cartId){
        Cart cart = cartRepo.findById(cartId).orElse(new Cart());
        cart.getCartItems().clear();
        cartRepo.save(cart);
        return true;
    }

    public cartResponse addToCart(cartRequest cartRequest){
        Cart cart = cartRepo.findById(cartRequest.getCartId()).orElse(null);
        if(cart == null){
            cart = new Cart();
            cart.setCartId(UUID.randomUUID());
            cart.setUsersId(cartRequest.getUsersId());
        }
        CartItems cartItems = modelMapper.map(cartRequest, CartItems.class);
        if(cart.getCartItems().indexOf(cartItems) != -1){
            cartItems.setQuantityAdded(cartItems.getQuantityAdded() + cartRequest.getQuantityAdded());
            Cart updatedCart = cartRepo.save(cart);
            cartResponse cartResponse = modelMapper.map(updatedCart, cartResponse.class);
            return cartResponse;
        }
        cart.getCartItems().addLast(cartItems);
        Cart updatedCart = cartRepo.save(cart);
        int index = updatedCart.getCartItems().indexOf(cartItems);
        cartResponse cartResponse = null;
        if(index != -1){
         cartResponse = modelMapper.map(updatedCart, cartResponse.class);
       }
       return cartResponse;
    }

}
