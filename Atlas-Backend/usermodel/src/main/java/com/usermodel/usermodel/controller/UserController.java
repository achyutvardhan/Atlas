package com.usermodel.usermodel.controller;


import java.util.Map;

import org.apache.hc.core5.http.HttpStatus;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usermodel.usermodel.model.User;
import com.usermodel.usermodel.services.userService;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private userService userService;
    
 
    @PostMapping("/register")
    public ResponseEntity<User> registerProfile(@RequestBody User user){
        User newUser = userService.registerProfile(user);
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(newUser);
    }
    
}
