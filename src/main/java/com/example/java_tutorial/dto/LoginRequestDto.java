package com.example.java_tutorial.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    @Schema(description = "email@example.com")
    @NotNull(message = "Email is required")
    private String email;
    
    @Schema(description = "********")
    @NotNull(message = "Password is required")
    private String password;
}
