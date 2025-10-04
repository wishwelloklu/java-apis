package com.example.java_tutorial.services;

import com.example.java_tutorial.dto.request.CreatePackageDto;
import com.example.java_tutorial.dto.responses.PackageResponse;
import com.example.java_tutorial.enums.PackageStatusEnum;

public interface PackageService {

    PackageResponse createPackage(CreatePackageDto createPackageDto);

    PackageResponse getPackage(String id);

    PackageResponse verifyPackage(String id, PackageStatusEnum status);

}
