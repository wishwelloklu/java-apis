package com.example.java_tutorial.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.java_tutorial.components.UserMapper;
import com.example.java_tutorial.config.SecurityConfig;
import com.example.java_tutorial.dto.request.AddUserDto;
import com.example.java_tutorial.dto.request.ChangePasswordDto;
import com.example.java_tutorial.dto.request.UpdateUserDto;
import com.example.java_tutorial.dto.responses.UserResponseDto;
import com.example.java_tutorial.enums.RoleEnum;
import com.example.java_tutorial.models.UserModel;
import com.example.java_tutorial.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto registerUser(AddUserDto addUserDto) {
        try {
            UserModel userModel = userMapper.toEntity(addUserDto);
            userModel.setRole(RoleEnum.USER);
            userModel.setPassword(securityConfig.passwordEncoder().encode(addUserDto.getPassword()));
            userModel = userRepository.save(userModel);
            return userMapper.toDto(userModel);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMessage();
            if (message.contains("users_email_unique")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists.");
            } else if (message.contains("users_phone_number_unique")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number already exists.");
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate value for a unique field.");
            }
        }
    }

    @Override
    public UserResponseDto login(String email, String password, String deviceToken) {

        UserModel userModel = userRepository.findByEmail(email);
        String passwordToMatch = userModel != null ? userModel.getPassword() : "fake";
        boolean isPasswordMatch = securityConfig.passwordEncoder().matches(password, passwordToMatch);
        if (userModel != null && isPasswordMatch) {
            if (deviceToken != null && !deviceToken.equals(userModel.getDeviceToken())) {
                userModel.setDeviceToken(deviceToken);
                userRepository.save(userModel);
            }
            return userMapper.toDto(userModel);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

    }

    @Override
    public UserResponseDto updateUser(UpdateUserDto updateUserDto, String email) {

        UserModel userModel = userRepository.findByEmail(email);
        if (userModel == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found");
        }
        userMapper.updateUserFromDto(updateUserDto, userModel);

        UserModel updatedUser = userRepository.save(userModel);

        return userMapper.toDto(updatedUser);
    }

    @Override
    public Boolean deleteUser(Long id, String email) {

        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID cannot be null");
        }
        UserModel userModel = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!userModel.getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this user");
        }
        userRepository.deleteById(id);
        return true;

    }

    @Override
    public Boolean fetchUser(String email) {
        try {
            UserModel userModel = userRepository.findByEmail(email);
            if (userModel != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UserResponseDto fetchUserByEmail(String email) {
        UserModel userModel = userRepository.findByEmail(email);
        if (userModel != null) {
            return userMapper.toDto(userModel);
        }
        return null;
    }

    @Override
    public boolean changePassword(String email, ChangePasswordDto changePasswordDto) {
        UserModel userModel = userRepository.findByEmail(email);
        if (userModel == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        boolean isPasswordMatch = securityConfig.passwordEncoder().matches(changePasswordDto.getOldPassword(),
                userModel.getPassword());
        if (!isPasswordMatch) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid password");
        }
        userModel.setPassword(securityConfig.passwordEncoder().encode(changePasswordDto.getNewPassword()));
        userRepository.save(userModel);
        return true;
    }

}
