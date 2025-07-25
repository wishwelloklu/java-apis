package com.example.java_tutorial.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddUserDto {
    @Schema(description = "First name", example = "Wishwell")
    @NotNull(message = "First name is required")
    private String firstName;

    @Schema(description = "Last name", example = "Oklu")
    @NotNull(message = "Last name is required")
    private String lastName;

    @Schema(description = "Phone number", example = "0241234567")
    @NotNull(message = "Phone number is required")
    @Size(min = 10, message = "phone number is")
    private String phoneNumber;

    @Schema(description = "Email", example = "email@example.com")
    @NotNull(message = "Email is required")
    private String email;

    @Schema(description = "Password", example = "**********")
    @NotNull(message = "Password is required")
    private String password;

    private String action;

}
