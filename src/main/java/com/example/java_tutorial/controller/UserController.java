package com.example.java_tutorial.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.java_tutorial.dto.AddUserDto;
import com.example.java_tutorial.dto.GenerateOtpDto;
import com.example.java_tutorial.dto.LoginRequestDto;
import com.example.java_tutorial.dto.UpdateUserDto;
import com.example.java_tutorial.dto.VerifyOtpDto;
import com.example.java_tutorial.dto.responses.ApiResponseDto;
import com.example.java_tutorial.dto.responses.UserResponseDto;
import com.example.java_tutorial.services.OtpServiceImpl;
import com.example.java_tutorial.services.RedisService;
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
    private OtpServiceImpl otpService;
    private RedisService redisService;

    public UserController(UserServiceImpl userService, OtpServiceImpl otpService, RedisService redisService) {
        this.userService = userService;
        this.otpService = otpService;
        this.redisService = redisService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<String>> addUser(@RequestBody AddUserDto userDto) {

        try {
            userDto.setAction("register");
            AddUserDto user = (AddUserDto) redisService.getObject(userDto.getEmail());
            System.out.println("user exist" + user != null);
            if (user != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(
                        new ApiResponseDto<>(
                                true,
                                "User with this email already exists",
                                null));
            }

            redisService.setObject(userDto.getEmail(), userDto);
            final String responseDto = otpService.generateOtp(userDto.getEmail());
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

    @PostMapping("/generate_otp")
    public ResponseEntity<ApiResponseDto<String>> generateOtp(@RequestBody GenerateOtpDto entity) {
        try {
            String otp = otpService.generateOtp(entity.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponseDto<>(
                            true,
                            "OTP generated successfully",
                            otp));
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

    @PostMapping("/verify_otp")
    public ResponseEntity<ApiResponseDto<Object>> verifyOtp(@RequestBody VerifyOtpDto verifyOtpDto) {
        Object respObject = new Object();
        try {
            Boolean isVerified = otpService.verifyOtp(verifyOtpDto.getOtp(), verifyOtpDto.getEmail());

            if (isVerified == null) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ApiResponseDto<>(
                                false,
                                "OTP has expired",
                                null));
            } else if (isVerified) {
                System.out.println("Here is " + isVerified);
                otpService.clearOtp(verifyOtpDto.getEmail());

                Object redisData = redisService.getObject(verifyOtpDto.getEmail());
                if (redisData == null) {
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ApiResponseDto<>(
                                    true,
                                    "OTP verified, but no user data found",
                                    null));
                }
                if (redisData.getClass() == AddUserDto.class) {
                    AddUserDto addUserDto = (AddUserDto) redisData;
                    respObject = userService.registerUser(addUserDto);
                } else if (redisData.getClass() == LoginRequestDto.class) {
                    LoginRequestDto loginRequestDto = (LoginRequestDto) redisData;
                    respObject = userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
                }
                if (respObject != null) {
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ApiResponseDto<>(
                                    true,
                                    "OTP verified successfully",
                                    respObject));
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ApiResponseDto<>(
                                    true,
                                    "OTP verified, but no user data found",
                                    null));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponseDto<>(
                                false,
                                "Invalid OTP",
                                null));
            }
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
