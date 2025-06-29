package com.example.java_tutorial.services;

import org.springframework.stereotype.Service;

import com.example.java_tutorial.dto.AddUserDto;
import com.example.java_tutorial.dto.responses.UserResponseDto;
import com.example.java_tutorial.models.UserModel;
import com.example.java_tutorial.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto addUser(AddUserDto addUserDto) {
        UserModel userModel = new UserModel();
        userModel.setFirstName(addUserDto.getFirstname());
        userModel.setLastName(addUserDto.getLastName());

        UserModel userModel2 = userRepository.save(userModel);
        UserResponseDto userResponseDto = new UserResponseDto(
                userModel2.getId(),
                userModel2.getFirstName(),
                userModel2.getLastName()
        );
        return userResponseDto;
    }
}
