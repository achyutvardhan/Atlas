package com.usermodel.usermodel.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.usermodel.usermodel.dto.MailDto;
import com.usermodel.usermodel.dto.MailResponse;

@FeignClient(name="sendemailservice" , path = "/email")
public interface SendEmailClient {
    @GetMapping("/send-mail")
    public MailResponse sendMail(MailDto mailDto);
}
