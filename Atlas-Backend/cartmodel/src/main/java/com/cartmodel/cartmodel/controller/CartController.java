package com.cartmodel.cartmodel.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cartmodel.cartmodel.dto.cartRequest;
import com.cartmodel.cartmodel.dto.cartResponse;
import com.cartmodel.cartmodel.services.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // addtocart
    @PostMapping("/add-to-cart")
    public ResponseEntity<cartResponse> addToCart(@RequestBody  cartRequest cartRequest){
        cartResponse response = cartService.addToCart(cartRequest);
        if(response == null){
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.ok(response);
    }
    // removefromcart
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeFromCart(@RequestBody UUID cartId, @RequestBody UUID cartItemId){
        boolean response = cartService.removeFromCart(cartId , cartItemId);
        if(response == false){
            return ResponseEntity.status(400).body("Item not found in cart");
        }
        return ResponseEntity.ok("Item removed from cart successfully");
    }

    // getcart
    @GetMapping("/{id}")
    public ResponseEntity<List<cartResponse>> getAllCartItem(@RequestBody UUID id) {
        List<cartResponse> response = cartService.getAllCartItem(id);
        return ResponseEntity.ok(response);
    }
    // clearcart
    @GetMapping("/clear/{id}")
    public ResponseEntity<String> clearCart(@RequestHeader UUID cartId) {
        boolean response = cartService.clearCart(cartId);
        if(response == false){
            return ResponseEntity.status(400).body("Cart not found");
        }
        return ResponseEntity.ok("Cart cleared successfully");
    }
    // checkout
}
