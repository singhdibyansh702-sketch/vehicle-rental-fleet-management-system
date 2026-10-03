package com.vehiclerental.controller;

import com.vehiclerental.dto.ApiResponse;
import com.vehiclerental.dto.VehicleDto;
import com.vehiclerental.model.*;
import com.vehiclerental.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Vehicle>>> getAllVehicles(
            @RequestParam(required = false) VehicleType type,
            @RequestParam(required = false) FuelType fuel,
            @RequestParam(required = false) Transmission transmission,
            @RequestParam(required = false) VehicleStatus status,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String keyword
    ) {
        List<Vehicle> vehicles = vehicleService.filterVehicles(type, fuel, transmission, status, maxPrice, keyword);
        return ResponseEntity.ok(ApiResponse.ok("Vehicles retrieved successfully", vehicles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Vehicle>> getVehicleById(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(ApiResponse.ok("Vehicle found", vehicle));
    }

    @GetMapping("/{id}/check-availability")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkAvailability(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long excludeBookingId
    ) {
        boolean isAvailable = vehicleService.checkAvailability(id, startDate, endDate, excludeBookingId);
        Map<String, Object> result = new HashMap<>();
        result.put("vehicleId", id);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("available", isAvailable);

        String message = isAvailable ? "Vehicle is available for the selected dates" : "Vehicle is not available for these dates";
        return ResponseEntity.ok(ApiResponse.ok(message, result));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Vehicle>> createVehicle(@Valid @RequestBody VehicleDto dto) {
        Vehicle created = vehicleService.createVehicle(dto);
        return new ResponseEntity<>(ApiResponse.ok("Vehicle added successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Vehicle>> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleDto dto) {
        Vehicle updated = vehicleService.updateVehicle(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("Vehicle updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok(ApiResponse.ok("Vehicle deleted successfully"));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Vehicle>> updateStatus(
            @PathVariable Long id,
            @RequestParam VehicleStatus status
    ) {
        Vehicle updated = vehicleService.updateVehicleStatus(id, status);
        return ResponseEntity.ok(ApiResponse.ok("Vehicle status updated to " + status, updated));
    }
}
