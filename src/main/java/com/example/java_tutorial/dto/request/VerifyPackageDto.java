package com.example.java_tutorial.dto.request;

import com.example.java_tutorial.enums.PackageStatusEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyPackageDto {

    private Long packageId;
    private PackageStatusEnum status;
}
