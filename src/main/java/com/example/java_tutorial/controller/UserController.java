package com.example.java_tutorial.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.java_tutorial.dto.AddUserDto;
import com.example.java_tutorial.dto.LoginRequestDto;
import com.example.java_tutorial.dto.UpdateUserDto;
import com.example.java_tutorial.dto.responses.ApiResponseDto;
import com.example.java_tutorial.dto.responses.UserResponseDto;
import com.example.java_tutorial.services.UserServiceImpl;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> addUser(@RequestBody AddUserDto userDto) {
        // System.out.println("Received request body: " + userDto.toString());
        try {
            final UserResponseDto responseDto = userService.addUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponseDto<>(
                            true,
                            "User creates successfully",
                            responseDto));
        } catch (ResponseStatusException e) {
            System.err.println("error" + e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(
                    new ApiResponseDto<>(
                            false,
                            e.getMessage(),
                            null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponseDto<>(
                            false,
                            e.getMessage(),
                            null)); // 500 Internal Server Error
        }
    }

    @PostMapping("/login")

    public ResponseEntity<ApiResponseDto<UserResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequest) {

        try {
            String message = "";
            UserResponseDto uResponseDto = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
            if (uResponseDto == null) {
                message = "No user found";
            } else {
                message = "Login successfully";
            }
            // tipsy23Adom
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponseDto<>(
                            uResponseDto != null,
                            message,
                            uResponseDto));
        } catch (ResponseStatusException e) {
            System.err.println("error" + e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(
                    new ApiResponseDto<>(
                            false,
                            e.getMessage(),
                            null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponseDto<>(
                            false,
                            e.getMessage(),
                            null)); // 500 Internal Server Error
        }
    }

    @PostMapping("/update_user/{id}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> postMethodName(
            @PathVariable Long id,
            @RequestBody UpdateUserDto entity) {

        try {
            UserResponseDto userResponseDto = userService.updateUser(entity, id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponseDto<>(
                            true,
                            "User updated successfully",
                            userResponseDto));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(
                    new ApiResponseDto<>(
                            false,
                            e.getMessage(),
                            null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponseDto<>(
                            false,
                            e.getMessage(),
                            null)); //
        }
    }


    @DeleteMapping("/delete_user/{id}")
    public ResponseEntity<ApiResponseDto<String>> deleteUser(@PathVariable Long id) {
        try {
            String message = userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponseDto<>(
                            true,
                            message,
                            null));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(
                    new ApiResponseDto<>(
                            false,
                            e.getMessage(),
                            null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponseDto<>(
                            false,
                            e.getMessage(),
                            null)); // 500 Internal Server Error
        }
    }

}
