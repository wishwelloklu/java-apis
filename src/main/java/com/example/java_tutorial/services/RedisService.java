package com.example.java_tutorial.services;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class RedisService {

    private  StringRedisTemplate stringRedisTemplate;
    private  RedisTemplate<String, Object> redisTemplate;

    public RedisService(StringRedisTemplate stringRedisTemplate,
            RedisTemplate<String, Object> redisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        System.out.println("StringRedisTemplate injected? " + (stringRedisTemplate != null));
        System.out.println("RedisTemplate injected? " + (redisTemplate != null));
    }

    // String operations
    public void setString(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void setStringWithExpiry(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // Object operations
    public void setObject(String key, Object value) {
       
        redisTemplate.opsForValue().set(key, value);
    }

    public Object getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Delete operations
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    // Check if key exists
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }
}
