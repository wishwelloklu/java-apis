package com.example.java_tutorial.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.java_tutorial.components.JwtUtil;
import com.example.java_tutorial.dto.request.AddUserDto;
import com.example.java_tutorial.dto.request.GenerateOtpDto;
import com.example.java_tutorial.dto.request.LoginRequestDto;
import com.example.java_tutorial.dto.request.RefreshTokenRequest;
import com.example.java_tutorial.dto.request.UpdateUserDto;
import com.example.java_tutorial.dto.request.VerifyOtpDto;
import com.example.java_tutorial.dto.responses.ApiResponseDto;
import com.example.java_tutorial.dto.responses.LoginResponse;
import com.example.java_tutorial.dto.responses.RegisterResponseDto;
import com.example.java_tutorial.dto.responses.UserResponseDto;
import com.example.java_tutorial.services.OtpServiceImpl;
import com.example.java_tutorial.services.RedisService;
import com.example.java_tutorial.services.UserService;
import com.example.java_tutorial.services.AuthService;
import com.example.java_tutorial.services.MailService;

import jakarta.validation.Valid;

import java.util.UUID;
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
@RequestMapping("/api/v1/auth")
public class UserController {
        private UserService userService;
        private OtpServiceImpl otpService;
        private RedisService redisService;
        private final JwtUtil jwtUtil;
        private final AuthService authService;
        private final MailService mailService;

        public UserController(UserService userService, OtpServiceImpl otpService, RedisService redisService,
                        AuthenticationManager authenticationManager, JwtUtil jwtUtil, AuthService authService,
                        MailService mailService) {
                this.userService = userService;
                this.otpService = otpService;
                this.redisService = redisService;
                this.jwtUtil = jwtUtil;
                this.authService = authService;
                this.mailService = mailService;
        }

        @PostMapping("/register")
        public ResponseEntity<ApiResponseDto<RegisterResponseDto>> addUser(@RequestBody AddUserDto userDto) {

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
                String otp = otpService.generateOtp(userDto.getEmail());
                System.err.println("otp " + otp);
                mailService.sendEmail(userDto.getEmail(), "Your OTP Code",
                                "Your One-Time Password (OTP) is: " + otp);

                RegisterResponseDto registerResponseDto = new RegisterResponseDto("An OTP has been sent to your email",
                                null);

                return ResponseEntity.status(HttpStatus.CREATED).body(
                                new ApiResponseDto<>(
                                                true,
                                                "Success",
                                                registerResponseDto));
        }

        @PostMapping("/login")
        public ResponseEntity<ApiResponseDto<LoginResponse>> login(@Valid @RequestBody LoginRequestDto loginRequest) {
                System.out.println("login called" + loginRequest.toString());
                String message = "";
                String token = "";
                String refreshToken = UUID.randomUUID().toString(); // Generate refresh token
                LoginResponse loginResponse = null;
                UserResponseDto uResponseDto = userService.login(loginRequest.getEmail(),
                                loginRequest.getPassword());

                message = "Login successfully";
                token = jwtUtil.generateToken(loginRequest.getEmail(), uResponseDto.getRole());
                // Store refresh token in Redis with a longer expiry (e.g., 7 days)
                redisService.setStringWithExpiry(refreshToken, loginRequest.getEmail(), 7, TimeUnit.DAYS);
                loginResponse = new LoginResponse(uResponseDto, token, refreshToken);

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
        public ResponseEntity<ApiResponseDto<String>> deleteUser(@PathVariable Long id,
                        @RequestHeader(value = "Authorization", required = false) String authHeader) {
                String email = authService.authenticateTokenAndExtractEmail(authHeader);
                Boolean value = userService.deleteUser(id, email);
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

        // eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3aXNod2VsbG9rbHVAZ21haWwuY29tIiwiaWF0IjoxNzY3NDYwMzE2LCJleHAiOjE3Njc0NjM5MTYsInJvbGUiOiJBRE1JTiJ9.eZlzpLT_ZY0nPuANPicPdgayZcDtWDO-po8YbhpUVRo
        // eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3aXNod2VsbG9rbHVAZ21haWwuY29tIiwiaWF0IjoxNzY3NDA5NTQ3LCJleHAiOjE3Njc0MTMxNDd9.EJTEHpi6OVv89pVYKd7BMvFwEhFu39Lq8lNYdofRFuU
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

        @PostMapping("/logout")
        public ResponseEntity<ApiResponseDto<Object>> logout(
                        @RequestHeader(value = "Authorization", required = false) String authHeader) {
                try {
                        authService.authenticateTokenAndExtractEmail(authHeader);
                        jwtUtil.clearToken(authHeader);
                        return ResponseEntity.status(HttpStatus.OK).body(
                                        new ApiResponseDto<>(
                                                        true,
                                                        "Logout success",
                                                        null));
                } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                                        new ApiResponseDto<>(
                                                        false,
                                                        "Logout error",
                                                        null));
                }

        }

        @PostMapping("/refresh-token")
        public ResponseEntity<ApiResponseDto<LoginResponse>> refreshToken(
                        @RequestBody RefreshTokenRequest refreshTokenRequest) {
                String refreshToken = refreshTokenRequest.getRefreshToken();
                String email = redisService.getString(refreshToken);

                if (email == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                                        new ApiResponseDto<>(false, "Invalid refresh token", null));
                }

                UserResponseDto userResponseDto = userService.fetchUserByEmail(email);
                if (userResponseDto == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                                        new ApiResponseDto<>(false, "User not found", null));
                }

                String newAccessToken = jwtUtil.generateToken(email, userResponseDto.getRole());
                String newRefreshToken = UUID.randomUUID().toString();

                redisService.delete(refreshToken);
                redisService.setStringWithExpiry(newRefreshToken, email, 7, TimeUnit.DAYS);

                LoginResponse loginResponse = new LoginResponse(userResponseDto, newAccessToken, newRefreshToken);

                return ResponseEntity.status(HttpStatus.OK).body(
                                new ApiResponseDto<>(true, "Token refreshed successfully", loginResponse));
        }

}
