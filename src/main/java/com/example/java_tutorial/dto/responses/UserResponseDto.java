package com.example.java_tutorial.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String role;

}