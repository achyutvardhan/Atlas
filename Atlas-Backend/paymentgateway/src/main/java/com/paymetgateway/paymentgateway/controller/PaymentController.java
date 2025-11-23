package com.paymetgateway.paymentgateway.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paymetgateway.paymentgateway.dto.CreateOrderDto;
import com.paymetgateway.paymentgateway.service.PaymentService;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderDto createOrder) throws RazorpayException {
        String order = paymentService.createOrder(createOrder);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/verify-payment/{userId}/{cartId}")
    public ResponseEntity<String> verifyPayment(@RequestParam("razorpay_order_id") String razorpayOrderId,
            @RequestParam("razorpay_payment_id") String razorpayPaymentId,
            @RequestParam("razorpay_signature") String razorpaySignature ,@PathVariable("userId") String userId , @PathVariable("cartId") String cartId ) throws RazorpayException {
        boolean isValid = paymentService.verifyPayment(razorpayOrderId, razorpayPaymentId, razorpaySignature ,userId,cartId);
        if (!isValid) {
            return ResponseEntity.status(400).body("Invalid payment signature");
        }
        return ResponseEntity.ok("Payment verified successfully");

    }

}
