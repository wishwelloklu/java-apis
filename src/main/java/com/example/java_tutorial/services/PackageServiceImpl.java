package com.example.java_tutorial.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.java_tutorial.dto.request.CreatePackageDto;
import com.example.java_tutorial.dto.responses.PackageResponse;
import com.example.java_tutorial.enums.PackageStatusEnum;
import com.example.java_tutorial.models.PackageModel;
import com.example.java_tutorial.repository.PackageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;

    @Override
    public PackageResponse createPackage(CreatePackageDto createPackageDto) {
        try {
            PackageModel packageModel = PackageModel.builder()
                    .mineralType(createPackageDto.getMineralType())
                    .quantity(createPackageDto.getQuantity())
                    .mineDate(createPackageDto.getMineDate())
                    .location(createPackageDto.getLocation())
                    .grade(createPackageDto.getGrade())
                    .minerId(createPackageDto.getMinerId())
                    .minerName(createPackageDto.getMinerName())
                    .notes(createPackageDto.getNotes())
                    .createdAt(
                            createPackageDto.getCreatedAt())
                    .status(PackageStatusEnum.pending)
                    .build();

            PackageModel savedPackage = packageRepository.save(packageModel);

            return PackageResponse.builder()
                    .id(savedPackage.getId())
                    .mineralType(savedPackage.getMineralType())
                    .quantity(savedPackage.getQuantity())
                    .mineDate(savedPackage.getMineDate())
                    .location(savedPackage.getLocation())
                    .grade(savedPackage.getGrade())
                    .minerId(savedPackage.getMinerId())
                    .minerName(savedPackage.getMinerName())
                    .notes(savedPackage.getNotes())
                    .createdAt(savedPackage.getCreatedAt())
                    .status(savedPackage.getStatus())
                    .build();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }

        catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());

        }

    }

    @Override
    public PackageResponse getPackage(String id) {
        try {
            PackageModel packageModel = packageRepository.getReferenceById(Long.valueOf(id));

            return PackageResponse.builder().id(packageModel.getId())
                    .mineralType(packageModel.getMineralType())
                    .quantity(packageModel.getQuantity())
                    .mineDate(packageModel.getMineDate())
                    .location(packageModel.getLocation())
                    .grade(packageModel.getGrade())
                    .minerId(packageModel.getMinerId())
                    .minerName(packageModel.getMinerName())
                    .notes(packageModel.getNotes())
                    .createdAt(packageModel.getCreatedAt())
                    .status(packageModel.getStatus())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());

        }
    }

    @Override
    public PackageResponse verifyPackage(String id, PackageStatusEnum status) {
        try {
            PackageModel packageModel = packageRepository.getReferenceById(Long.valueOf(id));
            packageModel.setStatus(status);
            PackageModel savedPackage = packageRepository.save(packageModel);
            return PackageResponse.builder()
                    .id(savedPackage.getId())
                    .mineralType(savedPackage.getMineralType())
                    .quantity(savedPackage.getQuantity())
                    .mineDate(savedPackage.getMineDate())
                    .location(savedPackage.getLocation())
                    .grade(savedPackage.getGrade())
                    .minerId(savedPackage.getMinerId())
                    .minerName(savedPackage.getMinerName())
                    .notes(savedPackage.getNotes())
                    .createdAt(savedPackage.getCreatedAt())
                    .status(savedPackage.getStatus())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());

        }
    }

}
