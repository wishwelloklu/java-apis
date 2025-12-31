package com.example.java_tutorial.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private UserResponseDto userResponseDto;
    private String token;
    private String refreshToken;

    public LoginResponse(UserResponseDto userResponseDto, String token, String refreshToken) {
        this.token = token;
        this.userResponseDto = userResponseDto;
        this.refreshToken = refreshToken;
    }
}
