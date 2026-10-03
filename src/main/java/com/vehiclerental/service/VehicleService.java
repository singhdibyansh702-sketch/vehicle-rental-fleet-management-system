package com.vehiclerental.service;

import com.vehiclerental.dto.VehicleDto;
import com.vehiclerental.model.*;

import java.time.LocalDate;
import java.util.List;

public interface VehicleService {
    List<Vehicle> getAllVehicles();
    List<Vehicle> filterVehicles(VehicleType type, FuelType fuel, Transmission transmission, VehicleStatus status, Double maxPrice, String keyword);
    Vehicle getVehicleById(Long id);
    Vehicle createVehicle(VehicleDto dto);
    Vehicle updateVehicle(Long id, VehicleDto dto);
    void deleteVehicle(Long id);
    Vehicle updateVehicleStatus(Long id, VehicleStatus status);
    boolean checkAvailability(Long vehicleId, LocalDate startDate, LocalDate endDate, Long excludeBookingId);
}
