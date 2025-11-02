package com.usermodel.usermodel.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.usermodel.usermodel.dto.AddressRequest;
import com.usermodel.usermodel.dto.AddressResponse;
import com.usermodel.usermodel.dto.LoginResponse;
import com.usermodel.usermodel.dto.MailDto;
import com.usermodel.usermodel.dto.MailResponse;
import com.usermodel.usermodel.dto.ProfileResponse;
import com.usermodel.usermodel.dto.RegistrationResponse;
import com.usermodel.usermodel.dto.userDTO;
import com.usermodel.usermodel.feign.SendEmailClient;
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

    @Autowired
    private SendEmailClient sendEmailClient;

    Logger log = LoggerFactory.getLogger(userService.class);

    public userDTO getUserById(UUID id) {
        User user = userRepo.findById(id).orElse(null);
        if (user == null)
            return new userDTO();
        userDTO userdto = new userDTO();
        userdto.setEmail(user.getUserDetails().getEmail());
        userdto.setPhoneNumber(user.getUserDetails().getPhoneNo());
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
    // String token = null;
    // if (authHeader != null && authHeader.startsWith("Bearer ")) {
    // token = authHeader.substring(7);
    // } else {
    // return null;
    // }
    // return token;
    // }

    public RegistrationResponse registerProfile(User user) {
        User existingUser = userRepo.findByUserName(user.getUserName());
        if (existingUser != null) {
            RegistrationResponse resp = new RegistrationResponse();
            resp.setMessage("User already exists");
            return resp;
        }
        User newUser = new User();
        newUser.setAddress(new ArrayList<>());
        newUser.setUserDetails(user.getUserDetails());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setUserName(user.getUserName());
        newUser.setRole(false);
        newUser.setVarified(false);

        String verificationToken = UUID.randomUUID().toString();
        newUser.setVerificationCode(verificationToken);
        userRepo.save(newUser);
        MailDto mailDto = new MailDto();
        mailDto.setTo(newUser.getUserDetails().getEmail());
        mailDto.setSubject("Please verify your email");
        mailDto.setMessage("Click the link to verify your email: http://localhost:8080/auth/verify-email?code="
                + verificationToken);
        MailResponse mailResponse = sendEmailClient.sendMail(mailDto);
        log.info("Mail response status: {}", mailResponse.isStatus());
        if (mailResponse.isStatus()) {
            RegistrationResponse resp = new RegistrationResponse();
            resp.setMessage(
                    "User registered successfully. Verification email sent to " + newUser.getUserDetails().getEmail());
            resp.setUserId(newUser.getUserId());
            resp.setUserName(newUser.getUserName());
            resp.setPassword(user.getPassword());
            resp.setVerificationCode(verificationToken);
            resp.setVarified(newUser.isVarified());
            resp.setUserDetails(newUser.getUserDetails());
            resp.setAddress(resp.getAddress());
            return resp;
        } else {
            RegistrationResponse resp = new RegistrationResponse();
            resp.setMessage("User registered successfully. However, failed to send verification email to "
                    + newUser.getUserDetails().getEmail());
            resp.setUserId(newUser.getUserId());
            resp.setUserName(newUser.getUserName());
            resp.setPassword(user.getPassword());
            resp.setVerificationCode(verificationToken);
            resp.setVarified(newUser.isVarified());
            resp.setUserDetails(newUser.getUserDetails());
            resp.setAddress(resp.getAddress());
            return resp;
        }

    }

    public RegistrationResponse registerAdminProfile(User user) {
        User existingUser = userRepo.findByUserName(user.getUserName());
        if (existingUser != null) {
            RegistrationResponse resp = new RegistrationResponse();
            resp.setMessage("User already exists");
            return resp;
        }
        User newUser = new User();
        newUser.setAddress(new ArrayList<>());
        newUser.setUserDetails(user.getUserDetails());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setUserName(user.getUserName());
        newUser.setRole(true); // Set role to true for admin
        newUser.setVarified(false);

        String verificationToken = UUID.randomUUID().toString();
        newUser.setVerificationCode(verificationToken);
        userRepo.save(newUser);
        MailDto mailDto = new MailDto();
        mailDto.setTo(newUser.getUserDetails().getEmail());
        mailDto.setSubject("Please verify your email");
        mailDto.setMessage("Click the link to verify your email: http://localhost:8080/auth/verify-email?code="
                + verificationToken);
        MailResponse mailResponse = sendEmailClient.sendMail(mailDto);
        if (mailResponse.isStatus()) {
            RegistrationResponse resp = new RegistrationResponse();
            resp.setMessage(
                    "User registered successfully. Verification email sent to " + newUser.getUserDetails().getEmail());
            resp.setUserId(newUser.getUserId());
            resp.setUserName(newUser.getUserName());
            resp.setPassword(user.getPassword());
            resp.setVerificationCode(verificationToken);
            resp.setVarified(newUser.isVarified());
            resp.setUserDetails(newUser.getUserDetails());
            resp.setAddress(resp.getAddress());
            return resp;
        } else {
            RegistrationResponse resp = new RegistrationResponse();
            resp.setMessage("User registered successfully. However, failed to send verification email to "
                    + newUser.getUserDetails().getEmail());
            resp.setUserId(newUser.getUserId());
            resp.setUserName(newUser.getUserName());
            resp.setPassword(user.getPassword());
            resp.setVerificationCode(verificationToken);
            resp.setVarified(newUser.isVarified());
            resp.setUserDetails(newUser.getUserDetails());
            resp.setAddress(resp.getAddress());
            return resp;
        }
    }

    public LoginResponse loginUser(String username, String password) {
        boolean isAuthenticated = authenticateUser(username, password);
        if (!isAuthenticated)
            return new LoginResponse();
        User user = userRepo.findByUserName(username);
        if (user != null && user.isVarified()) {
            LoginResponse lr = new LoginResponse();
            String token = jwtService.generateToken(user.getUserId(), user.getUserName(),
                    user.getUserDetails().getEmail(), user.isRole());
            user.setToken(token);
            lr.setToken(token);
            lr.setMessage("Login Successful");
            return lr;
        } else {
            if (user == null) {
                LoginResponse lr = new LoginResponse();
                lr.setMessage("User not found");
                return lr;
            }else {
                LoginResponse lr = new LoginResponse();
                lr.setMessage("Email not verified. Please verify your email before logging in.");
                return lr;
            }
        }
    }

    public boolean verifyEmail(String code ) {
        User user = userRepo.findByVerificationCode(code);
        if (user != null) {
            user.setVarified(true);
            user.setVerificationCode(null); // Clear the verification code after successful verification
            userRepo.save(user);
            return true;
        } else {
            return false;
        }
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
