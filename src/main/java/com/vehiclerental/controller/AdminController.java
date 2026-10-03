package com.vehiclerental.controller;

import com.vehiclerental.dto.ApiResponse;
import com.vehiclerental.dto.DashboardStatsDto;
import com.vehiclerental.dto.UserDto;
import com.vehiclerental.service.DashboardService;
import com.vehiclerental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private UserService userService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getDashboardStats() {
        DashboardStatsDto stats = dashboardService.getAdminStats();
        return ResponseEntity.ok(ApiResponse.ok("Fleet dashboard statistics", stats));
    }

    @GetMapping("/customers")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllCustomers() {
        List<UserDto> customers = userService.getAllCustomers();
        return ResponseEntity.ok(ApiResponse.ok("Customers list", customers));
    }
}
