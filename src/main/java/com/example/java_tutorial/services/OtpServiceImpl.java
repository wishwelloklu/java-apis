
package com.example.java_tutorial.services;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class OtpServiceImpl {

    private RedisService redisTemplate;

    public OtpServiceImpl(RedisService redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateOtp(String phoneOrEmail) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        redisTemplate.setStringWithExpiry("otp" + phoneOrEmail, otp, 1, TimeUnit.MINUTES); // 5 min expiry
        return otp;
    }

    public Boolean verifyOtp(String phoneOrEmail, String inputOtp) {
        String storedOtp = redisTemplate.getString("otp" + phoneOrEmail);
        return storedOtp != null && storedOtp.equals(inputOtp);
    }

    public void clearOtp(String phoneOrEmail) {
        redisTemplate.delete("otp" + phoneOrEmail);
    }
}
