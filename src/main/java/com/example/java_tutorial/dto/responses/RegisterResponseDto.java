package com.example.java_tutorial.dto.responses;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterResponseDto {
    private String message;
    private String otp;

    public RegisterResponseDto(String message, String otp) {
        this.message = message;
        this.otp = otp;
    }
}
