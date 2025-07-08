package com.example.java_tutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.java_tutorial.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByEmail(String email);
}
