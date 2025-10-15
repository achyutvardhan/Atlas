package com.usermodel.usermodel.controller;



import java.util.UUID;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usermodel.usermodel.dto.LoginRequest;
import com.usermodel.usermodel.dto.LoginResponse;
import com.usermodel.usermodel.dto.ProfileResponse;
import com.usermodel.usermodel.dto.userDTO;
import com.usermodel.usermodel.model.User;
import com.usermodel.usermodel.services.userService;



@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private userService userService;


    @GetMapping("/hello")
    public String getMethodName() {
        return "Hello World";
    }

    @GetMapping("/{id}")
    public ResponseEntity<userDTO> getUserById(@PathVariable UUID id){
        userDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
//    Users Registration API
    @PostMapping("/register")
    public ResponseEntity<User> registerProfile(@RequestBody User user){
        User newUser = userService.registerProfile(user);
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(newUser);
    }

// Login API
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginData){
        String username = loginData.getUsername();
        String password = loginData.getPassword();
        LoginResponse lr = userService.loginUser(username, password);
        return ResponseEntity.accepted().body("Token : " + lr.getToken() + " Message : " + lr.getMessage());
    }
// logout API
   @GetMapping("/logout")
   public ResponseEntity<String> logoutUser(){
    return ResponseEntity.accepted().body("Logged Out Successfully");
   }    

// get user profile API
   @GetMapping("/profile")
   public ResponseEntity<ProfileResponse> getUserProfile(@RequestHeader(name = "Authorization") String authHeader){
    ProfileResponse userDetails = userService.getUserProfile(authHeader);
    if (userDetails.getEmail() == null) {
        return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body(null);
    }
    return ResponseEntity.accepted().body(userDetails);

   }
}
