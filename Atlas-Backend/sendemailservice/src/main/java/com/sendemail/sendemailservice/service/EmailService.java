package com.sendemail.sendemailservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.sendemail.sendemailservice.dto.MailDto;
import com.sendemail.sendemailservice.dto.MailResponse;

@Service
public class EmailService {
    private final Logger log = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender mailSender;

    public MailResponse sendMail(MailDto mail)throws Exception {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("testatlas98@gmail.com");
            message.setTo(mail.getTo());
            message.setSubject(mail.getSubject());
            message.setText(mail.getMessage());
            mailSender.send(message);
    
            MailResponse mailResponse = new MailResponse();
            mailResponse.setMessage("Mail sent successfully to " + mail.getTo());
            mailResponse.setStatus(true);
            log.info("mail sent succesfully {}", mailResponse.isStatus());
            return mailResponse;

        }
        catch(Exception e){
            log.error("Error sending mail to {}: {}", mail.getTo(), e.getMessage(), e);
            MailResponse mailResponse = new MailResponse();
            mailResponse.setMessage("Error while sending mail to " + mail.getTo());
            mailResponse.setStatus(false);
            return mailResponse;
        }
    }
}
