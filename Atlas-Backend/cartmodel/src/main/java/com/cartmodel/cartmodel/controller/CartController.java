package com.cartmodel.cartmodel.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cartmodel.cartmodel.dto.cartRequest;
import com.cartmodel.cartmodel.dto.cartResponse;
import com.cartmodel.cartmodel.dto.orderResponse;
import com.cartmodel.cartmodel.services.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // addtocart
    @PostMapping("/add-to-cart")
    public ResponseEntity<cartResponse> addToCart(@RequestHeader(name = "Authorization") String authHeader,@RequestBody cartRequest cartRequest) {
        cartResponse response = cartService.addToCart(authHeader , cartRequest);
        if (response == null) {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.ok(response);
    }

    // removefromcart
    @DeleteMapping("/{cartId}/items/{cartItemId}")
    public ResponseEntity<String> removeFromCart(@RequestHeader(name = "Authorization") String authHeader,@PathVariable("cartId") UUID cartId, @PathVariable("cartItemId") UUID cartItemId) {
        boolean response = cartService.removeFromCart(authHeader,cartId, cartItemId);
        if (response == false) {
            return ResponseEntity.status(400).body("Item not found in cart");
        }
        return ResponseEntity.ok("Item removed from cart successfully");
    }

    // getcart
    @GetMapping("/{cartId}")
    public ResponseEntity<List<cartResponse>> getAllCartItem(@RequestHeader(name = "Authorization") String authHeader,@PathVariable UUID cartId) {
        List<cartResponse> response = cartService.getAllCartItem(authHeader,cartId);
        return ResponseEntity.ok(response);
    }

    // clearcart
    @GetMapping("/clear/{id}")
    public ResponseEntity<String> clearCart(@RequestHeader(name = "Authorization") String authHeader,@RequestBody UUID cartId) {
        boolean response = cartService.clearCart(authHeader,cartId);
        if (response == false) {
            return ResponseEntity.status(400).body("Cart not found");
        }
        return ResponseEntity.ok("Cart cleared successfully");
    }
    // checkout
    @GetMapping("/checkout/{cartId}")
    public ResponseEntity<orderResponse> checkoutCart(@RequestHeader(name = "Authorization") String authHeader,@PathVariable("cartId") UUID cartId)
    {
        orderResponse dto = cartService.checkoutCart(authHeader ,cartId);
        if(dto == null)
        return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    } 
}
