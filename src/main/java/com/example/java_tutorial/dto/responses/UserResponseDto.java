package com.example.java_tutorial.dto.responses;

import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    public UserResponseDto(Long id, String firstName, String lastName, String email, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

}