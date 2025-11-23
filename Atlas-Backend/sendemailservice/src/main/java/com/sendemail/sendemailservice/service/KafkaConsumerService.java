package com.sendemail.sendemailservice.service;

import com.sendemail.sendemailservice.dto.MailDto;
import com.sendemail.sendemailservice.dto.MailResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerService {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "otp-generator", groupId = "consumer-otp-generator-group-1")
    public void consumeOrderEvent(MailDto mailDto) {
        System.out.println("KafkaConsumerService: consumed message: " + mailDto);
        try {
            MailResponse resp =  emailService.sendMail(mailDto);
            System.out.println("Email sent status: " + resp.isStatus());
        } catch (Exception e) {
            System.err.println("Failed to parse or handle order-created message: " + e.getMessage());
        }
    }
}