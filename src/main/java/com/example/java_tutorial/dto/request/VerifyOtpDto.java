package com.example.java_tutorial.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpDto {
    private String email;
    private String otp;
}
