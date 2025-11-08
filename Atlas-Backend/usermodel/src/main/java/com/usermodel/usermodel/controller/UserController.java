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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.usermodel.usermodel.dto.LoginRequest;
import com.usermodel.usermodel.dto.LoginResponse;
import com.usermodel.usermodel.dto.ProfileResponse;
import com.usermodel.usermodel.dto.RegistrationResponse;
import com.usermodel.usermodel.dto.userDTO;
import com.usermodel.usermodel.model.User;
import com.usermodel.usermodel.services.userService;



@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private userService userService;

    @GetMapping("/{id}")
    public ResponseEntity<userDTO> getUserById(@PathVariable UUID id){
        System.out.println(id);
        userDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
//    Users Registration API
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerProfile(@RequestBody User user){
        RegistrationResponse newUser = userService.registerProfile(user);
        System.out.println("hey there");
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(newUser);
    }
// Admin registration API can be added similarly
   @PostMapping("/admin/v1/register")
    public ResponseEntity<RegistrationResponse> registerAdminProfile(@RequestBody User user){
        RegistrationResponse newUser = userService.registerAdminProfile(user);
        System.out.println("hey there");
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
   public ResponseEntity<ProfileResponse> getUserProfile(@RequestHeader(name = "X-USER-ID") String userId){
    ProfileResponse userDetails = userService.getUserProfile(userId);
    if (userDetails.getEmail() == null) {
        return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body(null);
    }
    return ResponseEntity.accepted().body(userDetails);

   }

// verify email API
   @GetMapping("/verify-email")
   public ResponseEntity<String> verifyEmail(@RequestParam String code ){
    boolean isVerified = userService.verifyEmail(code);
    if(isVerified){
        return ResponseEntity.ok("Email verified successfully.");
    } else {
        return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Invalid verification code.");
    }
   }

}
