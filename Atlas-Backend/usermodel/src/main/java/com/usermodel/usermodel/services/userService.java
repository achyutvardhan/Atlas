package com.usermodel.usermodel.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.usermodel.usermodel.dto.AddressRequest;
import com.usermodel.usermodel.dto.AddressResponse;
import com.usermodel.usermodel.dto.LoginResponse;
import com.usermodel.usermodel.dto.ProfileResponse;
import com.usermodel.usermodel.dto.userDTO;
import com.usermodel.usermodel.model.Address;
import com.usermodel.usermodel.model.User;
import com.usermodel.usermodel.repo.UserRepository;


@Service
public class userService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public userDTO getUserById(UUID id) {
        User user = userRepo.findById(id).orElse(null);
        if (user == null)
            return new userDTO();
        userDTO userdto = new userDTO();
        userdto.setUserId(user.getUserId());
        userdto.setUserName(user.getUserName());
        return userdto;
    }

    public boolean authenticateUser(String username, String password) {
        User user = userRepo.findByUserName(username);
        if (user == null)
            return false;
        if (passwordEncoder.matches(password, user.getPassword()))
            return true;
        else
            return false;
    }

    // public String checkToken(String authHeader) {
    //     String token = null;
    //     if (authHeader != null && authHeader.startsWith("Bearer ")) {
    //         token = authHeader.substring(7);
    //     } else {
    //         return null;
    //     }
    //     return token;
    // }

    public User registerProfile(User user) {
        User existingUser = userRepo.findByUserName(user.getUserName());
        if (existingUser != null)
            return existingUser;
        User newUser = new User();
        newUser.setAddress(new ArrayList<>());
        newUser.setUserDetails(user.getUserDetails());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setUserName(user.getUserName());
        newUser.setRole(false);
        return userRepo.save(newUser);
    }

    public User registerAdminProfile(User user) {
        User existingUser = userRepo.findByUserName(user.getUserName());
        if (existingUser != null)
            return existingUser;
        User newUser = new User();
        newUser.setAddress(new ArrayList<>());
        newUser.setUserDetails(user.getUserDetails());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setUserName(user.getUserName());
        newUser.setRole(true); // Set role to true for admin
        return userRepo.save(newUser);
    }

    public LoginResponse loginUser(String username, String password) {
        boolean isAuthenticated = authenticateUser(username, password);
        if (!isAuthenticated)
            return new LoginResponse();
        User user = userRepo.findByUserName(username);
        if (user != null) {
            LoginResponse lr = new LoginResponse();
            String token = jwtService.generateToken(user.getUserId(), user.getUserName(), user.getUserDetails().getEmail(),user.isRole());
            user.setToken(token);
            lr.setToken(token);
            lr.setMessage("Login Successful");
            return lr;
        } else
            return new LoginResponse();
    }

    public String logoutUser(String token) {
        return "Logged Out Successfully";
    }

    public ProfileResponse getUserProfile(String userId) {
        User user = userRepo.findById(UUID.fromString(userId)).orElse(null);
        if (user == null)
            return new ProfileResponse();
        ProfileResponse pr = new ProfileResponse();
        pr.setEmail(user.getUserDetails().getEmail());
        pr.setPhoneNo(user.getUserDetails().getPhoneNo());
        pr.setUsername(user.getUserName());
        return pr;
    }

    public AddressResponse saveAddress(String userId, AddressRequest adr) {
        User user = userRepo.findById(UUID.fromString(userId)).orElse(null);
        if (user == null)
            return new AddressResponse();
        Address address = new Address();
        address.setStreet(adr.getStreet());
        address.setCity(adr.getCity());
        address.setState(adr.getState());
        address.setPincode(adr.getPincode());
        user.getAddress().add(address);
        userRepo.save(user);
        AddressResponse adrep = new AddressResponse();
        adrep.setStreet(address.getStreet());
        adrep.setCity(address.getCity());
        adrep.setState(address.getState());
        adrep.setPincode(address.getPincode());
        return adrep;
    }

    public List<AddressResponse> getAllUserAddress(String userId) {
        User user = userRepo.findById(UUID.fromString(userId)).orElse(null);
        if (user == null)
            return new ArrayList<>();
        List<Address> addresses = user.getAddress();
        List<AddressResponse> addressResponses = new ArrayList<>();
        for (Address addr : addresses) {
            AddressResponse adrep = new AddressResponse();
            adrep.setStreet(addr.getStreet());
            adrep.setCity(addr.getCity());
            adrep.setState(addr.getState());
            addressResponses.add(adrep);
        }
        return addressResponses;
    }
}
