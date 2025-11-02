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

import com.cartmodel.cartmodel.dto.CreateOrderDto;
import com.cartmodel.cartmodel.dto.ProductDto;
import com.cartmodel.cartmodel.dto.Userdto;
import com.cartmodel.cartmodel.dto.cartRequest;
import com.cartmodel.cartmodel.dto.cartResponse;
import com.cartmodel.cartmodel.feign.PaymentGateway;
import com.cartmodel.cartmodel.feign.ProductClient;
import com.cartmodel.cartmodel.feign.UserClient;
import com.cartmodel.cartmodel.model.Cart;
import com.cartmodel.cartmodel.model.CartItems;
import com.cartmodel.cartmodel.repo.CartRepo;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private PaymentGateway paymentGateway;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserClient userClient;

    // public String checkToken(String authHeader){
    // String token = null;
    // if (authHeader != null && authHeader.startsWith("Bearer ")) {
    // token = authHeader.substring(7);
    // } else {
    // return null;
    // }
    // return token;
    // }
    // private static final Logger logger =
    // LoggerFactory.getLogger(CartService.class);

    public List<cartResponse> getAllCartItem(String userId, UUID cartId) {
        UUID userid = UUID.fromString(userId);
        Cart carts = cartRepo.findByUserId(userid); // userID from token
        if (carts == null || carts.getCartItems().isEmpty() || !carts.getCartId().equals(cartId))
            return null;

        List<cartResponse> cartResponses = carts.getCartItems().stream().map(
                cartItem -> {
                    cartResponse cartResponse = modelMapper.map(cartItem, cartResponse.class);
                    cartResponse.setCartId(carts.getCartId());
                    return cartResponse;
                }).collect(Collectors.toList());
        return cartResponses;
    }

    public boolean removeFromCart(String userId, UUID cartId, UUID cartItemId) {
        UUID userid = UUID.fromString(userId);
        // get userId from token and set to cartRequest
        Cart cart = cartRepo.findByUserId(userid);
        if (cart == null || cart.getCartItems() == null || !cart.getCartId().equals(cartId))
            return false;
        boolean removed = cart.getCartItems().removeIf(item -> item.getCartItemsId().equals(cartItemId));
        if (removed)
            cartRepo.save(cart);
        return removed;
    }

    public boolean clearCart(String userId, UUID cartId) {
        UUID userid = UUID.fromString(userId);
        Cart cart = cartRepo.findByUserId(cartId);
        if (cart.getUserId() != userid)
            return false;
        if (cart == null || cart.getCartItems() == null || !cart.getCartId().equals(cartId))
            return false;
        if (cart.getCartItems().isEmpty())
            return true;
        cart.getCartItems().clear();
        cartRepo.save(cart);
        return true;
    }

    public cartResponse addToCart(String userId, cartRequest cartRequest) {
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
        ProductDto productDto = productClient.getProductById(cartRequest.getProductId());
        if (productDto == null) {
            cartResponse cartResp = new cartResponse();
            cartResp.setProductId(cartRequest.getProductId());
            cartResp.setProductName(cartRequest.getProductName());
            cartResp.setCartId(cart.getCartId());
            cartResp.setMessage("Product not found");
            return cartResp;
        }

        if (productDto.isInStock() == false) {
            cartResponse cartResp = new cartResponse();
            cartResp.setProductId(cartRequest.getProductId());
            cartResp.setProductName(cartRequest.getProductName());
            cartResp.setCartId(cart.getCartId());
            cartResp.setMessage("Product is out of stock");
            return cartResp;
        }

        if (existing != null) {
            int availableQuantity = productDto.getProductQuantity() - existing.getQuantityAdded();
            if (availableQuantity < cartRequest.getQuantityAdded()) {
                cartResponse cartResp = new cartResponse();
                cartResp.setProductId(cartRequest.getProductId());
                cartResp.setProductName(cartRequest.getProductName());
                cartResp.setCartId(cart.getCartId());
                cartResp.setMessage("Only " + availableQuantity + " items left in stock");
                return cartResp;
            }
            existing.setQuantityAdded(existing.getQuantityAdded() + cartRequest.getQuantityAdded());
        } else {
            if (productDto.getProductQuantity() < cartRequest.getQuantityAdded()) {
                cartResponse cartResp = new cartResponse();
                cartResp.setProductId(cartRequest.getProductId());
                cartResp.setProductName(cartRequest.getProductName());
                cartResp.setCartId(cart.getCartId());
                cartResp.setMessage("Only " + productDto.getProductQuantity() + " items left in stock");
                return cartResp;
            }
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

    public String checkoutCart(String userId, UUID cartId , String shippingAddress) throws Exception {

        try {
            UUID userid = UUID.fromString(userId);
            Cart cart = cartRepo.findByUserId(userid);
            if (cart == null || cart.getCartItems() == null || !cart.getCartId().equals(cartId))
                return "Cart not found";
            CreateOrderDto orderDto = new CreateOrderDto();
            orderDto.setAmount(cart.getCartItems().stream()
                    .mapToInt(item -> {
                        ProductDto product = productClient.getProductById(item.getProductId());
                        return item.getQuantityAdded() * product.getPrice();
                    })
                    .sum() * 100); // amount in smallest currency unit
            orderDto.setCurrency("INR");
            orderDto.setReceiptId(UUID.randomUUID());
            orderDto.setUserId(userid);
            orderDto.setCartId(cart.getCartId());
            // Fetch user details
            Userdto userDetails = userClient.getUserById(userid);
            if (userDetails == null) {
                throw new Exception("User not found");
            }
            orderDto.setEmail(userDetails.getEmail());
            orderDto.setPhoneNumber(userDetails.getPhoneNumber());
            orderDto.setShippingAddress(shippingAddress);
            String order = paymentGateway.createOrder(orderDto);
            cartRepo.delete(cart);
            return order;
            
        } catch (Exception e) {
            throw new Exception("Error during checkout: " + e.getMessage());
        }
    }

}
