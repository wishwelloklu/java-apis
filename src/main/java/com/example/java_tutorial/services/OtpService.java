package com.example.java_tutorial.services;


public interface OtpService {

    
    String generateOtp(String email);

    Boolean verifyOtp(String otp, String email);

    void clearOtp(String email);
}
