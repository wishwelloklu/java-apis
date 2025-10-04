package com.example.java_tutorial.services;

import com.example.java_tutorial.dto.request.AddUserDto;
import com.example.java_tutorial.dto.request.UpdateUserDto;
import com.example.java_tutorial.dto.responses.UserResponseDto;

public interface UserService {

    UserResponseDto registerUser(AddUserDto addUserDto);

    UserResponseDto login(String email, String password);

    UserResponseDto updateUser(UpdateUserDto updateUserDto, String email);

    Boolean deleteUser(Long id);

    Boolean fetchUser(String email);

}
