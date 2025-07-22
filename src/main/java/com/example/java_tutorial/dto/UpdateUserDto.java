package com.example.java_tutorial.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateUserDto {
    @Schema(description = "First name", example = "Wishwell")
    private String firstName;

    @Schema(description = "Last name", example = "Oklu")
    private String lastName;

    @Schema(description = "Phone number", example = "0241234567")
    @Size(min = 10,message = "phone number is")
    private String phoneNumber;

    @Schema(description = "Email", example = "email@example.com")
    private String email;

}
