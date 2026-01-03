package com.example.java_tutorial.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.java_tutorial.dto.request.CreatePackageDto;
import com.example.java_tutorial.dto.request.VerifyPackageDto;
import com.example.java_tutorial.dto.responses.ApiResponseDto;
import com.example.java_tutorial.dto.responses.PackageResponse;
import com.example.java_tutorial.models.UserModel;
import com.example.java_tutorial.services.AuthService;
import com.example.java_tutorial.services.PackageServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/packages")
@RequiredArgsConstructor
public class PackageController {
        private final AuthService authService;
        private final PackageServiceImpl packageServiceImpl;

        @PostMapping("/create")
        public ResponseEntity<ApiResponseDto<PackageResponse>> createPackage(
                        @Valid @RequestBody CreatePackageDto entity,
                        @RequestHeader(value = "Authorization", required = false) String authHeader) {

                String email = authService.authenticateTokenAndExtractEmail(authHeader);
                UserModel miner = authService.getUserByEmail(email);

                PackageResponse packageResponse = packageServiceImpl.createPackage(entity, miner);

                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDto<>(
                                true,
                                "Package created successfully",
                                packageResponse));

        }

        @GetMapping("/")
        public ResponseEntity<ApiResponseDto<ArrayList<PackageResponse>>> getPackages(
                        @RequestHeader(value = "Authorization", required = false) String authHeader) {
                System.out.println("Token " + authHeader);
                final String email = authService.authenticateTokenAndExtractEmail(authHeader);
                ArrayList<PackageResponse> allPackages = packageServiceImpl.getAllPackages(email);

                return ResponseEntity.status(HttpStatus.OK)
                                .body(new ApiResponseDto<>(true, "Packages fetched", allPackages));
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping("/verify")
        public ResponseEntity<ApiResponseDto<PackageResponse>> postMethodName(@RequestBody VerifyPackageDto entity,
                        @RequestHeader(value = "Authorization", required = false) String authHeader) {

                authService.authenticateTokenAndExtractEmail(authHeader);
                PackageResponse packageResponse = packageServiceImpl.verifyPackage(entity.getPackageId().toString(),
                                entity.getStatus());

                return ResponseEntity.status(HttpStatus.OK)
                                .body(new ApiResponseDto<>(true, "Package verified", packageResponse));

        }

}
