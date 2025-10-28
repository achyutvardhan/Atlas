package com.sendemail.sendemailservice.dto;

import lombok.Data;

@Data
public class MailResponse {
    private String message;
    private boolean status;
}
