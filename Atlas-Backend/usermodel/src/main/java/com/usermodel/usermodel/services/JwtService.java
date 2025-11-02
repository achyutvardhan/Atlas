package com.usermodel.usermodel.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private static final String SECRET_KEY = "u7D9fX2qLp8sVb3N6kT1hR4cY0mZ5wQg";

     private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(UUID userID, String userName , String email , boolean role) {
        Map<String , Object> claims = new HashMap<>();
        claims.put("username", userName);
        claims.put("email" , email);
        claims.put("role" , role);
        return Jwts.builder()
               .claims(claims)
               .subject(userID.toString())
               .issuedAt(new Date())
               .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))// 1 hr expiration
               .signWith(getSigningKey())
               .compact();
    }
}
