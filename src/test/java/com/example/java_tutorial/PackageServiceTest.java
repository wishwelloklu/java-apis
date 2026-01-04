package com.example.java_tutorial;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.java_tutorial.dto.request.CreatePackageDto;
import com.example.java_tutorial.dto.responses.PackageResponse;
import com.example.java_tutorial.enums.PackageStatusEnum;
import com.example.java_tutorial.enums.RoleEnum;
import com.example.java_tutorial.models.PackageModel;
import com.example.java_tutorial.models.UserModel;
import com.example.java_tutorial.repository.PackageRepository;
import com.example.java_tutorial.services.PackageServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PackageServiceTest {

    @Mock
    private PackageRepository packageRepository;

    @InjectMocks
    private PackageServiceImpl packageService;

    @Test
    public void testCreatePackage_Success() {
        // 1. Arrange (Prepare valid data)
        UserModel miner = new UserModel(1L, "John", "Doe", "123456", "pass", "john@test.com", RoleEnum.USER);

        CreatePackageDto dto = new CreatePackageDto();
        dto.setMineralType("Gold");
        dto.setQuantity(10.0);
        dto.setMineDate(Date.valueOf("2023-01-01"));
        dto.setLocation("Site A");
        dto.setGrade("High");
        dto.setNotes("Test notes");

        // Mock the repository to return a 'Saved' package when .save() is called
        PackageModel savedPackage = PackageModel.builder()
                .id("100") // Simulate DB ID assignment
                .mineralType("Gold")
                .quantity(10.0)
                .miner(miner) // Link the miner
                .status(PackageStatusEnum.pending)
                .build();

        when(packageRepository.save(any(PackageModel.class))).thenReturn(savedPackage);

        // 2. Act (Call the method)
        PackageResponse response = packageService.createPackage(dto, miner);

        // 3. Assert (Check results)
        assertNotNull(response.getId());
        assertEquals("Gold", response.getMineralType());
        assertEquals(10.0, response.getQuantity());
        assertEquals("1", response.getMinerId()); // Check if ID conversion works
        assertEquals("John Doe", response.getMinerName()); // Check name concatenation
    }
}
