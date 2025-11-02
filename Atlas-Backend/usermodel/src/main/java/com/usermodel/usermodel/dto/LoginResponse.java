package com.usermodel.usermodel.dto;

import lombok.Data;

@Data
public class LoginResponse {
    String token;
    String message;
}
