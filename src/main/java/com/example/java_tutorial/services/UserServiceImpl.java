package com.example.java_tutorial.services;

import java.util.Objects;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.java_tutorial.config.SecurityConfig;
import com.example.java_tutorial.dto.request.AddUserDto;
import com.example.java_tutorial.dto.request.UpdateUserDto;
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
    public UserResponseDto registerUser(AddUserDto addUserDto) {
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
    public UserResponseDto updateUser(UpdateUserDto updateUserDto, String email) {
        try {
            UserModel userModel = userRepository.findByEmail(email);
            if (userModel == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found");
            }

            if (Objects.nonNull(updateUserDto.getEmail()) && updateUserDto.getEmail() != "") {
                userModel.setEmail(updateUserDto.getEmail());
            }

            if (Objects.nonNull(updateUserDto.getFirstName()) && updateUserDto.getFirstName() != "") {
                userModel.setFirstName(updateUserDto.getFirstName());
            }

            if (Objects.nonNull(updateUserDto.getPhoneNumber()) && updateUserDto.getPhoneNumber() != "") {
                userModel.setPhoneNumber(updateUserDto.getPhoneNumber());
            }

            if (Objects.nonNull(updateUserDto.getLastName()) && updateUserDto.getLastName() != "") {
                userModel.setLastName(updateUserDto.getLastName());
            }

            UserModel newUserModel = userRepository.save(userModel);

            UserResponseDto userResponseDto = new UserResponseDto(
                    newUserModel.getId(),
                    newUserModel.getFirstName(),
                    newUserModel.getLastName(),
                    newUserModel.getEmail(),
                    newUserModel.getPhoneNumber());

            return userResponseDto;
        } catch (Exception e) {
            System.out.println("newUserModel:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean deleteUser(Long id) {
        try {
            Boolean isUserExist = userRepository.findById(id).isPresent();
            if (isUserExist) {
                userRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
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

}
