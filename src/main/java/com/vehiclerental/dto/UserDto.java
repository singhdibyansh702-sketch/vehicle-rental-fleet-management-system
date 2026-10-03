package com.vehiclerental.dto;

import com.vehiclerental.model.Role;
import com.vehiclerental.model.User;

import java.time.LocalDateTime;

public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String drivingLicenseNumber;
    private Role role;
    private LocalDateTime createdAt;

    public UserDto() {
    }

    public UserDto(User user) {
        if (user != null) {
            this.id = user.getId();
            this.name = user.getName();
            this.email = user.getEmail();
            this.phone = user.getPhone();
            this.address = user.getAddress();
            this.drivingLicenseNumber = user.getDrivingLicenseNumber();
            this.role = user.getRole();
            this.createdAt = user.getCreatedAt();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
