
package com.example.java_tutorial.services;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class OtpServiceImpl {

    private StringRedisTemplate redisTemplate;

    @Cacheable(value = "cache")
    public String generateOtp(String phoneOrEmail) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        redisTemplate.opsForValue().set(phoneOrEmail, otp, 5, TimeUnit.MINUTES); // 5 min expiry
        return otp;
    }

   
    public Boolean verifyOtp(String phoneOrEmail, String inputOtp) {
        String storedOtp = redisTemplate.opsForValue().get(phoneOrEmail);
        return storedOtp != null && storedOtp.equals(inputOtp);
    }


    public void clearOtp(String phoneOrEmail) {
        redisTemplate.delete(phoneOrEmail);
    }
}
