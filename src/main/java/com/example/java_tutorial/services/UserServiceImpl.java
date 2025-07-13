package com.example.java_tutorial.services;

import java.util.Objects;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.java_tutorial.config.SecurityConfig;
import com.example.java_tutorial.dto.AddUserDto;
import com.example.java_tutorial.dto.UpdateUserDto;
import com.example.java_tutorial.dto.responses.UserResponseDto;
import com.example.java_tutorial.models.UserModel;
import com.example.java_tutorial.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private SecurityConfig securityConfig;

    public UserServiceImpl(UserRepository userRepository, SecurityConfig securityConfig) {
        this.userRepository = userRepository;
        this.securityConfig = securityConfig;

    }

    @Override
    public UserResponseDto addUser(AddUserDto addUserDto) {
        try {
            UserModel userModel = new UserModel();
            userModel.setFirstName(addUserDto.getFirstName());
            userModel.setLastName(addUserDto.getLastName());
            userModel.setEmail(addUserDto.getEmail());
            userModel.setPhoneNumber(addUserDto.getPhoneNumber());
            userModel.setPassword(securityConfig.passwordEncoder().encode(addUserDto.getPassword()));

            UserModel userModel2 = userRepository.save(userModel);
            UserResponseDto userResponseDto = new UserResponseDto(
                    userModel2.getId(),
                    userModel2.getFirstName(),
                    userModel2.getLastName(),
                    userModel2.getEmail(),
                    userModel2.getPhoneNumber());
            return userResponseDto;
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
    public UserResponseDto login(String email, String password) {
        
        UserModel userModel = userRepository.findByEmail(email);
        if (userModel != null) {
            boolean isPasswordMatch = securityConfig.passwordEncoder().matches(password, userModel.getPassword());
            if (isPasswordMatch) {
                UserResponseDto userResponseDto = new UserResponseDto(
                        userModel.getId(),
                        userModel.getFirstName(),
                        userModel.getLastName(),
                        userModel.getEmail(),
                        userModel.getPhoneNumber());

                return userResponseDto;
            }
        }
        return null;
    }

    @Override
    public UserResponseDto updateUser(UpdateUserDto updateUserDto, Long id) {

        try {
            UserModel userModel = userRepository.findById(id).get();

            if (Objects.nonNull(updateUserDto.getEmail()) && updateUserDto.getEmail() != "") {
                userModel.setEmail(updateUserDto.getEmail());
            }

            if (Objects.nonNull(updateUserDto.getFirstName()) && updateUserDto.getFirstName() != "") {
                userModel.setEmail(updateUserDto.getFirstName());
            }

            if (Objects.nonNull(updateUserDto.getPhoneNumber()) && updateUserDto.getPhoneNumber() != "") {
                userModel.setEmail(updateUserDto.getPhoneNumber());
            }

            if (Objects.nonNull(updateUserDto.getLastName()) && updateUserDto.getLastName() != "") {
                userModel.setEmail(updateUserDto.getLastName());
            }

            userRepository.save(userModel);

            UserResponseDto userResponseDto = new UserResponseDto(
                    userModel.getId(),
                    userModel.getFirstName(),
                    userModel.getLastName(),
                    userModel.getEmail(),
                    userModel.getPhoneNumber());

            return userResponseDto;
        } catch (Exception e) {
            e.printStackTrace();
            
            return null;
        }
    }

    @Override
    public String deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            return "User deleted successfully";
        } catch (Exception e) {

            return e.toString();
        }
    }

}
