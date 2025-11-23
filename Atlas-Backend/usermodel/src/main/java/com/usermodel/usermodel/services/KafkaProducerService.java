package com.usermodel.usermodel.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.usermodel.usermodel.dto.MailDto;


@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, MailDto> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, MailDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmailEvent(MailDto mailDto) {
        kafkaTemplate.send("otp-generator", mailDto);
    }
}
