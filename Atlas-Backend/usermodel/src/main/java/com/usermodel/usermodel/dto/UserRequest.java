package com.usermodel.usermodel.dto;

import java.sql.Date;
import java.util.List;

import com.usermodel.usermodel.model.Address;
import com.usermodel.usermodel.model.UserDetails;

public class UserRequest {
    private String username;
    private String password;
    private String token;
    private Date tokenExpiration;
    private UserDetails userDetails;
    private List<Address> address;
}
