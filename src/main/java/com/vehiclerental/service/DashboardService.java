package com.vehiclerental.service;

import com.vehiclerental.dto.DashboardStatsDto;

import java.util.Map;

public interface DashboardService {
    DashboardStatsDto getAdminStats();
    Map<String, Object> getCustomerStats(String customerEmail);
}
