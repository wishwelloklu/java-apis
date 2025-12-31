package com.example.java_tutorial.dto.request;

import java.sql.Date;

import com.example.java_tutorial.enums.PackageStatusEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreatePackageDto {
    @Schema(description = "Mineral type", example = "Gold")
    @NotNull(message = "Mineral type is required")
    private String mineralType;

    @Schema(description = "Quantity of the mineral in kilograms", example = "10.5")
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private double quantity;

    @Schema(description = "Date of mining", example = "2023-12-25")
    @NotNull(message = "Mine date is required")
    private Date mineDate;

    @Schema(description = "Location of the mine", example = "Tarkwa")
    @NotNull(message = "Location is required")
    private String location;

    @Schema(description = "Grade of the mineral", example = "High")
    @NotNull(message = "Grade is required")
    private String grade;

    @Schema(description = "Date of creation", example = "2023-12-25")
    @NotNull(message = "Creation date is required")
    private Date createdAt;

    @Schema(description = "Status of the package", example = "PENDING")
    @NotNull(message = "Status is required")
    private PackageStatusEnum status;

    @Schema(description = "Additional notes", example = "Handle with care")
    private String notes;
}