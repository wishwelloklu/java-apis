package com.example.java_tutorial.dto.responses;

import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long id;
    private String firstName;
    private String lastName;

    public UserResponseDto(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}