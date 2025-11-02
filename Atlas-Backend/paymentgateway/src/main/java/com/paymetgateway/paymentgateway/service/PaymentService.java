package com.paymetgateway.paymentgateway.service;

import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.razorpay.Utils;
import com.netflix.discovery.converters.Auto;
import com.paymetgateway.paymentgateway.dto.CreateOrderDto;
import com.paymetgateway.paymentgateway.dto.orderResponse;
import com.paymetgateway.paymentgateway.feign.OrderClient;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class PaymentService {

    @Autowired
    private OrderClient orderClient;

    private static final String RAZORPAY_KEY_ID = ;
    private static final String RAZORPAY_KEY_SECRET = ;

    public String createOrder(CreateOrderDto createOrder) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", createOrder.getAmount()); // amount in the smallest currency unit
        orderRequest.put("currency", createOrder.getCurrency());
        orderRequest.put("receipt", createOrder.getReceiptId().toString());
        orderRequest.put("userId", createOrder.getUserId().toString());
        orderRequest.put("cartId", createOrder.getCartId().toString());
        orderRequest.put("shippingAddress", createOrder.getShippingAddress());
        orderRequest.put("email", createOrder.getEmail());
        orderRequest.put("phoneNumber", createOrder.getPhoneNumber());
        Order order = razorpay.orders.create(orderRequest);
        return order.toString();
    }

    public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature,
            String userId, String cartId) throws RazorpayException {

        try {
            String payload = razorpayOrderId + '|' + razorpayPaymentId;
            boolean isValid = Utils.verifySignature(payload, razorpaySignature, RAZORPAY_KEY_SECRET);

            // update order
            UUID userid = UUID.fromString(userId);
            UUID cartid = UUID.fromString(cartId);
            orderResponse dto = orderClient.placeOrder(userid, cartid);
            if (dto == null)
                return false;
            return isValid;

        } catch (RazorpayException e) {
            throw e;
        } catch (Exception e) {
            throw new RazorpayException("General Exception during callback");
        }

    }
}
