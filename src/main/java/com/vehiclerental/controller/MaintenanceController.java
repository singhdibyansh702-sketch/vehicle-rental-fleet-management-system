package com.vehiclerental.controller;

import com.vehiclerental.dto.ApiResponse;
import com.vehiclerental.dto.MaintenanceRequest;
import com.vehiclerental.model.Maintenance;
import com.vehiclerental.model.MaintenanceStatus;
import com.vehiclerental.service.MaintenanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Maintenance>>> getAllMaintenance() {
        List<Maintenance> list = maintenanceService.getAllMaintenance();
        return ResponseEntity.ok(ApiResponse.ok("Maintenance records retrieved", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Maintenance>> getMaintenanceById(@PathVariable Long id) {
        Maintenance maintenance = maintenanceService.getMaintenanceById(id);
        return ResponseEntity.ok(ApiResponse.ok("Maintenance record", maintenance));
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<ApiResponse<List<Maintenance>>> getByVehicle(@PathVariable Long vehicleId) {
        List<Maintenance> list = maintenanceService.getMaintenanceByVehicle(vehicleId);
        return ResponseEntity.ok(ApiResponse.ok("Vehicle maintenance history", list));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Maintenance>> scheduleMaintenance(@Valid @RequestBody MaintenanceRequest request) {
        Maintenance created = maintenanceService.scheduleMaintenance(request);
        return new ResponseEntity<>(ApiResponse.ok("Vehicle scheduled for maintenance successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Maintenance>> updateMaintenance(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceRequest request
    ) {
        Maintenance updated = maintenanceService.updateMaintenance(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Maintenance record updated successfully", updated));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Maintenance>> updateStatus(
            @PathVariable Long id,
            @RequestParam MaintenanceStatus status
    ) {
        Maintenance updated = maintenanceService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.ok("Maintenance status updated to " + status, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMaintenance(@PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.ok(ApiResponse.ok("Maintenance record removed"));
    }
}
