package com.example.java_tutorial.dto.responses;

import java.sql.Date;

import com.example.java_tutorial.enums.PackageStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageResponse {

    private Long id;

    private String mineralType;

    private double quantity;

    private Date mineDate;

    private String location;

    private String grade;

    private String minerId;

    private String minerName;

    private Date createdAt;

    private PackageStatusEnum status;

    private String notes;
}