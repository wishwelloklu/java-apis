package com.example.java_tutorial.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserDto {
    @Schema(description = "First name", example = "Wishwell")
    @NotNull(message = "First name is required")
    private String firstname;

    @Schema(description = "Last name", example = "Oklu")
    @NotNull(message = "Last name is required")
    private String lastName;

    @Schema(description = "Phone number", example = "0241234567")
    @NotNull(message = "Phone number is required")
    private String phoneNumber;

    @Schema(description = "Email", example = "email@example.com")
    @NotNull(message = "Email is required")
    private String email;

}
