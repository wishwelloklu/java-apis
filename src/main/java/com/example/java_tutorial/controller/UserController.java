package com.example.java_tutorial.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.java_tutorial.components.JwtUtil;
import com.example.java_tutorial.dto.request.AddUserDto;
import com.example.java_tutorial.dto.request.GenerateOtpDto;
import com.example.java_tutorial.dto.request.LoginRequestDto;
import com.example.java_tutorial.dto.request.UpdateUserDto;
import com.example.java_tutorial.dto.request.VerifyOtpDto;
import com.example.java_tutorial.dto.responses.ApiResponseDto;
import com.example.java_tutorial.dto.responses.LoginResponse;
import com.example.java_tutorial.dto.responses.UserResponseDto;
import com.example.java_tutorial.services.OtpServiceImpl;
import com.example.java_tutorial.services.RedisService;
import com.example.java_tutorial.services.UserServiceImpl;
import com.example.java_tutorial.services.AuthService;

import jakarta.validation.Valid;

import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
        private UserServiceImpl userService;
        private OtpServiceImpl otpService;
        private RedisService redisService;
        private final JwtUtil jwtUtil;
        private final AuthService authService;

        public UserController(UserServiceImpl userService, OtpServiceImpl otpService, RedisService redisService,
                        AuthenticationManager authenticationManager, JwtUtil jwtUtil, AuthService authService) {
                this.userService = userService;
                this.otpService = otpService;
                this.redisService = redisService;
                this.jwtUtil = jwtUtil;
                this.authService = authService;
        }

        @PostMapping("/register")
        public ResponseEntity<ApiResponseDto<String>> addUser(@RequestBody AddUserDto userDto) {

                System.out.println("registration called");
                userDto.setAction("register");

                AddUserDto user = (AddUserDto) redisService.getObject(userDto.getEmail());
                Boolean userExist = userService.fetchUser(userDto.getEmail());
                System.out.println("user exist" + user != null);
                if (user != null || userExist) {
                        return ResponseEntity.status(HttpStatus.CREATED).body(
                                        new ApiResponseDto<>(
                                                        true,
                                                        "User with this email already exists",
                                                        null));
                }

                redisService.setObjectWithExpiry(userDto.getEmail(), userDto, 1, TimeUnit.MINUTES);
                String responseDto = otpService.generateOtp(userDto.getEmail());

                return ResponseEntity.status(HttpStatus.CREATED).body(
                                new ApiResponseDto<>(
                                                true,
                                                "User creates successfully",
                                                responseDto));
        }

        @PostMapping("/login")
        public ResponseEntity<ApiResponseDto<LoginResponse>> login(@Valid @RequestBody LoginRequestDto loginRequest) {
                System.out.println("login called" + loginRequest.toString());
                String message = "";
                String token = "";
                LoginResponse loginResponse = null;
                UserResponseDto uResponseDto = userService.login(loginRequest.getEmail(),
                                loginRequest.getPassword());
                if (uResponseDto == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                                        new ApiResponseDto<>(
                                                        false,
                                                        "Invalid credentials",
                                                        null));
                }

                message = "Login successfully";
                token = jwtUtil.generateToken(loginRequest.getEmail());
                loginResponse = new LoginResponse(uResponseDto, token);

                // tipsy23Adom
                return ResponseEntity.status(HttpStatus.OK).body(
                                new ApiResponseDto<>(
                                                uResponseDto != null,
                                                message,
                                                loginResponse));
        }

        @PostMapping("/update_user")
        public ResponseEntity<ApiResponseDto<UserResponseDto>> updateUser(

                        @RequestBody UpdateUserDto entity,
                        @RequestHeader(value = "Authorization", required = false) String authHeader) {

                String email = authService.authenticateTokenAndExtractEmail(authHeader);

                UserResponseDto userResponseDto = userService.updateUser(entity, email);
                if (userResponseDto == null) {
                        return ResponseEntity.status(HttpStatus.OK).body(
                                        new ApiResponseDto<>(
                                                        false,
                                                        "No user found",
                                                        null));
                }
                return ResponseEntity.status(HttpStatus.OK).body(
                                new ApiResponseDto<>(
                                                true,
                                                "User updated successfully",
                                                userResponseDto));
        }

        @DeleteMapping("/delete_user/{id}")
        public ResponseEntity<ApiResponseDto<String>> deleteUser(@PathVariable Long id) {
                Boolean value = userService.deleteUser(id);
                if (value) {
                        return ResponseEntity.status(HttpStatus.OK).body(
                                        new ApiResponseDto<>(
                                                        value,
                                                        "User deleted successful",
                                                        null));
                }
                return ResponseEntity.status(HttpStatus.OK).body(
                                new ApiResponseDto<>(
                                                value,
                                                "User not found",
                                                null));
        }

        @PostMapping("/generate_otp")
        public ResponseEntity<ApiResponseDto<String>> generateOtp(@RequestBody GenerateOtpDto entity) {
                String otp = otpService.generateOtp(entity.getEmail());
                return ResponseEntity.status(HttpStatus.OK).body(
                                new ApiResponseDto<>(
                                                true,
                                                "OTP generated successfully",
                                                otp));
        }

        @PostMapping("/verify_otp")
        public ResponseEntity<ApiResponseDto<Object>> verifyOtp(@RequestBody VerifyOtpDto verifyOtpDto) {
                Object respObject = new Object();
                Boolean isVerified = otpService.verifyOtp(verifyOtpDto.getEmail(), verifyOtpDto.getOtp());

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
                                redisService.delete(verifyOtpDto.getEmail());
                        } else if (redisData.getClass() == LoginRequestDto.class) {
                                LoginRequestDto loginRequestDto = (LoginRequestDto) redisData;
                                respObject = userService.login(loginRequestDto.getEmail(),
                                                loginRequestDto.getPassword());

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
        }

}
