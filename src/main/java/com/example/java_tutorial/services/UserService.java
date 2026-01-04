package com.example.java_tutorial.services;

import com.example.java_tutorial.dto.request.AddUserDto;
import com.example.java_tutorial.dto.request.ChangePasswordDto;
import com.example.java_tutorial.dto.request.UpdateUserDto;
import com.example.java_tutorial.dto.responses.UserResponseDto;

public interface UserService {

    UserResponseDto registerUser(AddUserDto addUserDto);

    UserResponseDto login(String email, String password, String deviceToken);

    UserResponseDto updateUser(UpdateUserDto updateUserDto, String email);

    Boolean deleteUser(Long id, String email);

    Boolean fetchUser(String email);

    UserResponseDto fetchUserByEmail(String email);

    boolean changePassword(String email, ChangePasswordDto changePasswordDto);

}
