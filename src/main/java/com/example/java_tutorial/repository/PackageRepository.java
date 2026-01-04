package com.example.java_tutorial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.java_tutorial.models.PackageModel;

@Repository
public interface PackageRepository extends JpaRepository<PackageModel, String> {
    List<PackageModel> findAllByMinerId(Long minerId);
}