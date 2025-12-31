package com.example.java_tutorial.services;

import java.util.ArrayList;

import com.example.java_tutorial.dto.request.CreatePackageDto;
import com.example.java_tutorial.dto.responses.PackageResponse;
import com.example.java_tutorial.enums.PackageStatusEnum;
import com.example.java_tutorial.models.UserModel;

public interface PackageService {

    PackageResponse createPackage(CreatePackageDto createPackageDto,UserModel miner);

    PackageResponse getPackage(String id);

    PackageResponse verifyPackage(String id, PackageStatusEnum status);

    ArrayList<PackageResponse> getAllPackages(String minerId);

}
