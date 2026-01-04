package com.example.java_tutorial.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.example.java_tutorial.dto.request.CreatePackageDto;
import com.example.java_tutorial.dto.responses.PackageResponse;
import com.example.java_tutorial.enums.PackageStatusEnum;
import com.example.java_tutorial.models.PackageModel;
import com.example.java_tutorial.models.UserModel;
import com.example.java_tutorial.repository.PackageRepository;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;
    private final AuthService authService;
    private final NotificationService notificationService;

    @Override
    public PackageResponse createPackage(CreatePackageDto createPackageDto, UserModel miner) {

        PackageModel packageModel = PackageModel.builder()
                .id(String.valueOf(Instant.now().toEpochMilli())) // Generate Epoch Millis ID
                .mineralType(createPackageDto.getMineralType())
                .quantity(createPackageDto.getQuantity())
                .mineDate(createPackageDto.getMineDate())
                .location(createPackageDto.getLocation())
                .grade(createPackageDto.getGrade())
                .miner(miner)
                .notes(createPackageDto.getNotes())
                .createdAt(
                        createPackageDto.getCreatedAt())
                .status(PackageStatusEnum.pending)
                .build();

        if (packageModel == null) {
            throw new IllegalArgumentException("Package cannot be null");
        }

        PackageModel savedPackage = packageRepository.save(packageModel);

        return PackageResponse.builder()
                .id(savedPackage.getId())
                .mineralType(savedPackage.getMineralType())
                .quantity(savedPackage.getQuantity())
                .mineDate(savedPackage.getMineDate())
                .location(savedPackage.getLocation())
                .grade(savedPackage.getGrade())
                .minerId(miner.getId().toString())
                .minerName(miner.getFirstName() + " " + miner.getLastName())
                .notes(savedPackage.getNotes())
                .createdAt(savedPackage.getCreatedAt())
                .status(savedPackage.getStatus())
                .build();

    }

    @Override
    public PackageResponse getPackage(String id) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Package ID cannot be null or empty");
        }
        PackageModel packageModel = packageRepository.findById(id).orElse(null);
        if (packageModel == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found");
        }

        return PackageResponse.builder().id(packageModel.getId())
                .mineralType(packageModel.getMineralType())
                .quantity(packageModel.getQuantity())
                .mineDate(packageModel.getMineDate())
                .location(packageModel.getLocation())
                .grade(packageModel.getGrade())
                .minerId(packageModel.miner.getId().toString())
                .minerName(packageModel.miner.getFirstName() + " " + packageModel.miner.getLastName())
                .notes(packageModel.getNotes())
                .createdAt(packageModel.getCreatedAt())
                .status(packageModel.getStatus())
                .build();

    }

    @Override
    public PackageResponse verifyPackage(String id, PackageStatusEnum status) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Package ID cannot be null or empty");
        }

        PackageModel packageModel = packageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Package not found"));
        packageModel.setStatus(status);
        PackageModel savedPackage = packageRepository.save(packageModel);
        notificationService.sendNotification(packageModel.miner, "Package Status Updated",
                "Your package status has been updated to " + status, "STATUS_UPDATE", null);
        return PackageResponse.builder()
                .id(savedPackage.getId())
                .mineralType(savedPackage.getMineralType())
                .quantity(savedPackage.getQuantity())
                .mineDate(savedPackage.getMineDate())
                .location(savedPackage.getLocation())
                .grade(savedPackage.getGrade())
                .minerId(savedPackage.miner.getId().toString())
                .minerName(savedPackage.miner.getFirstName() + " " + savedPackage.miner.getLastName())
                .notes(savedPackage.getNotes())
                .createdAt(savedPackage.getCreatedAt())
                .status(savedPackage.getStatus())
                .build();

    }

    @Override
    public ArrayList<PackageResponse> getAllPackages(String email) {
        String minerId = authService.getUserByEmail(email).getId().toString();
        List<PackageModel> packages = packageRepository.findAllByMinerId(Long.valueOf(minerId));
        return packages.stream()
                .map(packageModel -> PackageResponse.builder()
                        .id(packageModel.getId())
                        .mineralType(packageModel.getMineralType())
                        .quantity(packageModel.getQuantity())
                        .mineDate(packageModel.getMineDate())
                        .location(packageModel.getLocation())
                        .grade(packageModel.getGrade())
                        .minerId(packageModel.miner.getId().toString())
                        .minerName(packageModel.miner.getFirstName() + " " + packageModel.miner.getLastName())
                        .notes(packageModel.getNotes())
                        .createdAt(packageModel.getCreatedAt())
                        .status(packageModel.getStatus())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
