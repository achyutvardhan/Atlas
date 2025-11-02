package com.usermodel.usermodel.dto;

import java.util.List;
import java.util.UUID;

import com.usermodel.usermodel.model.Address;
import com.usermodel.usermodel.model.UserDetails;
import lombok.Data;

@Data
public class RegistrationResponse {
    private String message;
    private UUID userId;
    private String userName;
    private String password;
    private String token;
    private boolean role; // true for admin, false for regular user
    private String VerificationCode;
    private boolean isVarified;
    private UserDetails userDetails;
    private List<Address> address;
}
