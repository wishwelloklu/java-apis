package com.example.java_tutorial.services;

import org.springframework.web.server.ResponseStatusException;

import com.example.java_tutorial.models.UserModel;

public interface AuthService {
    String authenticateTokenAndExtractEmail(String authHeader) throws ResponseStatusException;
    UserModel getUserByEmail(String email);
}
