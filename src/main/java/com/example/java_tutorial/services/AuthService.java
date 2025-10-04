package com.example.java_tutorial.services;

import org.springframework.web.server.ResponseStatusException;

public interface AuthService {
    String authenticateTokenAndExtractEmail(String authHeader) throws ResponseStatusException;
}
