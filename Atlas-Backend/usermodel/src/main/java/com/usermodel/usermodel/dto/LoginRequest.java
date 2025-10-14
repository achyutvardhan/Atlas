package com.usermodel.usermodel.dto;

import lombok.Data;

@Data
public class LoginRequest {
    String email;
    String password;
}
