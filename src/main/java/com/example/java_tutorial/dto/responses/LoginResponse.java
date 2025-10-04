package com.example.java_tutorial.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private UserResponseDto userResponseDto;
    private String token;

    public LoginResponse(UserResponseDto userResponseDto,String token) {
        this.token = token;
        this.userResponseDto = userResponseDto;
    }
}
