package com.vehiclerental.service;

import com.vehiclerental.dto.MaintenanceRequest;
import com.vehiclerental.model.Maintenance;
import com.vehiclerental.model.MaintenanceStatus;

import java.util.List;

public interface MaintenanceService {
    Maintenance scheduleMaintenance(MaintenanceRequest request);
    Maintenance updateMaintenance(Long id, MaintenanceRequest request);
    Maintenance updateStatus(Long id, MaintenanceStatus status);
    List<Maintenance> getAllMaintenance();
    List<Maintenance> getMaintenanceByVehicle(Long vehicleId);
    Maintenance getMaintenanceById(Long id);
    void deleteMaintenance(Long id);
}
