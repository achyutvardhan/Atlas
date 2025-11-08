package com.sendemail.sendemailservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/send-mail")
    public ResponseEntity<MailResponse> sendMail(@RequestBody MailDto mailDto) throws Exception {
        MailResponse mailResponse = emailService.sendMail(mailDto);
        return ResponseEntity.ok(mailResponse);

    }

}
