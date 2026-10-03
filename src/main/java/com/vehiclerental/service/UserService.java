package com.vehiclerental.service;

import com.vehiclerental.dto.AuthRequest;
import com.vehiclerental.dto.AuthResponse;
import com.vehiclerental.dto.RegisterRequest;
import com.vehiclerental.dto.UserDto;
import com.vehiclerental.model.User;

import java.util.List;

public interface UserService {
    User register(RegisterRequest request);
    AuthResponse login(AuthRequest request);
    User getCurrentUser();
    User getUserById(Long id);
    User getUserByEmail(String email);
    List<UserDto> getAllCustomers();
    UserDto updateProfile(String email, RegisterRequest request);
}
