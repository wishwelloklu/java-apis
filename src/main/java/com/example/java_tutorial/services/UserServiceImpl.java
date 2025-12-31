package com.example.java_tutorial.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.java_tutorial.config.SecurityConfig;
import com.example.java_tutorial.dto.request.AddUserDto;
import com.example.java_tutorial.dto.request.UpdateUserDto;
import com.example.java_tutorial.dto.responses.UserResponseDto;
import com.example.java_tutorial.enums.RoleEnum;
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
            userModel.setRole(RoleEnum.USER);
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
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    @Override
    public UserResponseDto updateUser(UpdateUserDto updateUserDto, String email) {

        UserModel userModel = userRepository.findByEmail(email);
        if (userModel == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found");
        }

        if (updateIfExist(updateUserDto.getEmail())) {
            userModel.setEmail(updateUserDto.getEmail());
        }

        if (updateIfExist(updateUserDto.getFirstName())) {
            userModel.setFirstName(updateUserDto.getFirstName());
        }

        if (updateIfExist(updateUserDto.getPhoneNumber())) {
            userModel.setPhoneNumber(updateUserDto.getPhoneNumber());
        }

        if (updateIfExist(updateUserDto.getLastName())) {
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
            UserResponseDto userResponseDto = new UserResponseDto(
                    userModel.getId(),
                    userModel.getFirstName(),
                    userModel.getLastName(),
                    userModel.getEmail(),
                    userModel.getPhoneNumber());
            return userResponseDto;
        }
        return null;
    }

    public boolean updateIfExist(String value) {
        return value != null && !value.isBlank();
    }

}
