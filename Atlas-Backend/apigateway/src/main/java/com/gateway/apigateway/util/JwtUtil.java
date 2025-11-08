
package com.gateway.apigateway.util;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private static  String SECRET_KEY;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private Claims extractAllClaims(String Token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(Token)
                .getPayload();
    }

    // extract UUID
    public UUID extractUserId(String token) {
        return UUID.fromString(extractAllClaims(token).getSubject());
    }
    // extract role
    public Boolean extractUserRole(String token) {
        return extractAllClaims(token).get("role", Boolean.class);
    }
    // check Token expiration
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // validated token
    public boolean validateToken(String token, UUID userId) {
        final UUID extractUserID = extractUserId(token);
        return (extractUserID.equals(userId) && !isTokenExpired(token));
    }

}