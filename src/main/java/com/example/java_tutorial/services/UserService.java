package com.example.java_tutorial.services;

import com.example.java_tutorial.dto.AddUserDto;
import com.example.java_tutorial.dto.UpdateUserDto;
import com.example.java_tutorial.dto.responses.UserResponseDto;

public interface UserService {

    UserResponseDto addUser(AddUserDto addUserDto);

    UserResponseDto login(String email, String password);

    UserResponseDto updateUser(UpdateUserDto updateUserDto, Long id);

    String deleteUser(Long id);
    
}
