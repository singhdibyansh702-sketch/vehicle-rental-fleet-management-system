package com.vehiclerental.controller;

import com.vehiclerental.dto.*;
import com.vehiclerental.model.User;
import com.vehiclerental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@Valid @RequestBody RegisterRequest request) {
        User createdUser = userService.register(request);
        return new ResponseEntity<>(
                ApiResponse.ok("Customer registration successful. You can now login.", new UserDto(createdUser)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.ok("User profile retrieved", new UserDto(user)));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto>> updateProfile(@RequestBody RegisterRequest request, Authentication authentication) {
        UserDto updated = userService.updateProfile(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.ok("Profile updated successfully", updated));
    }
}
