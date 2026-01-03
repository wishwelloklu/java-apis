package com.example.java_tutorial.components;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import com.example.java_tutorial.services.RedisService;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final String SECRET_KEY = "replace_this_with_a_long_secure_key_for_signing";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    private final RedisService redisService;

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("role", role)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public void clearToken(String token) {
        // Blacklist the token by storing it in Redis with its remaining expiration time
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
        long ttl = expiration.getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            redisService.setStringWithExpiry(token, "blacklisted", ttl, TimeUnit.MILLISECONDS);
        }
    }

    public boolean validateToken(String token) {
        try {
            // Check if the token is blacklisted
            if (redisService.hasKey(token)) {
                return false;
            }
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
