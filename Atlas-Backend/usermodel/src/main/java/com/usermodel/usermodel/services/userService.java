package com.usermodel.usermodel.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.usermodel.usermodel.dto.AddressResponse;
import com.usermodel.usermodel.dto.LoginResponse;
import com.usermodel.usermodel.dto.ProfileResponse;
import com.usermodel.usermodel.model.Address;
import com.usermodel.usermodel.model.User;
import com.usermodel.usermodel.model.UserDetails;
import com.usermodel.usermodel.repo.AddressRepository;
import com.usermodel.usermodel.repo.UserDetailsRepository;
import com.usermodel.usermodel.repo.UserRepository;

@Service
public class userService {
     
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserDetailsRepository userDetailsRepo;
    @Autowired
    private AddressRepository addressRepo;

      @Autowired
    private PasswordEncoder passwordEncoder;


    public boolean authenticateUser(String email, String password){
        User user = userRepo.findByEmail(email);
        if(user == null) return false;
        if(passwordEncoder.matches(password, user.getPassword())) return true;
        else return false;
    }

     public String checkToken(String authHeader){
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); 
        } else {
            return null;
        }
        return token;
    }

    public User registerProfile(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public LoginResponse loginUser(String username , String password){
        boolean isAuthenticated = authenticateUser(username, password);
        if(!isAuthenticated) return new LoginResponse();
        User user = userRepo.findByUsername(username);
        if(user != null){
            LoginResponse lr = new LoginResponse();
            lr.setToken(user.getToken());
            lr.setMessage("Login Successful");
            return lr;
        }
        else return new LoginResponse();
    }

    public String logoutUser(String token){
        return "Logged Out Successfully";
    }

    public ProfileResponse getUserProfile(String authHeader){
        String token = checkToken(authHeader);
        if(token == null) return new ProfileResponse();
        User user = userRepo.findByToken(token);
        if(user == null) return new ProfileResponse();
        ProfileResponse pr = new ProfileResponse();
        pr.setEmail(user.getUserDetails().getEmail());
        pr.setPhoneNo(user.getUserDetails().getPhoneNo());
        return pr;
    }


    public AddressResponse saveAddress(String authHeader , AddressResponse adr){
        String token = checkToken(authHeader);
        if(token == null) return new AddressResponse();
        User user = userRepo.findByToken(token);
        if(user == null) return new AddressResponse();
        Address address = new Address();
        address.setStreet(adr.getStreet());
        address.setCity(adr.getCity());
        address.setState(adr.getState());
        Address savedAddress = addressRepo.save(address);
        AddressResponse adrep = new AddressResponse();
        adrep.setStreet(savedAddress.getStreet());
        adrep.setCity(savedAddress.getCity());
        adrep.setState(savedAddress.getState());
        return adrep;
    }


    public List<AddressResponse> getAllUserAddress(String authHeader){
        String token = checkToken(authHeader);
        if(token == null) return new ArrayList<>();
        User user = userRepo.findByToken(token);
        if(user == null) return new ArrayList<>();
        List<Address> addresses = user.getAddress();
        List<AddressResponse> addressResponses = new ArrayList<>();
        for(Address addr : addresses){
            AddressResponse adrep = new AddressResponse();
            adrep.setStreet(addr.getStreet());
            adrep.setCity(addr.getCity());
            adrep.setState(addr.getState());
            addressResponses.add(adrep);
        }
        return addressResponses;
    }
}
