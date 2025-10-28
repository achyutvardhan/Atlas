package com.sendemail.sendemailservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sendemail.sendemailservice.dto.MailDto;
import com.sendemail.sendemailservice.dto.MailResponse;
import com.sendemail.sendemailservice.service.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @GetMapping("/send-mail")
    public ResponseEntity<MailResponse> sendMail(MailDto mailDto)throws Exception {
        MailResponse mailResponse = emailService.sendMail(mailDto);
        if (mailResponse.isStatus()) {
            return ResponseEntity.ok(mailResponse);
        } else {
            return ResponseEntity.status(500).body(mailResponse);
        }
    }
    

}
