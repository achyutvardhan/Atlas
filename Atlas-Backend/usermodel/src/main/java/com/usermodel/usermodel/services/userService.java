package com.usermodel.usermodel.services;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public User registerProfile(User user)
    {
        return userRepo.save(user);
    }
}
