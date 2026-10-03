package com.vehiclerental.service;

import com.vehiclerental.dto.MaintenanceRequest;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.model.*;
import com.vehiclerental.repository.MaintenanceRepository;
import com.vehiclerental.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class MaintenanceServiceImpl implements MaintenanceService {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public Maintenance scheduleMaintenance(MaintenanceRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + request.getVehicleId()));

        Maintenance maintenance = new Maintenance();
        maintenance.setVehicle(vehicle);
        maintenance.setDescription(request.getDescription().trim());
        maintenance.setMaintenanceDate(request.getMaintenanceDate());
        maintenance.setCost(request.getCost());
        maintenance.setStatus(request.getStatus() != null ? request.getStatus() : MaintenanceStatus.SCHEDULED);
        maintenance.setServiceProvider(request.getServiceProvider() != null ? request.getServiceProvider().trim() : "Authorized Center");
        maintenance.setNotes(request.getNotes() != null ? request.getNotes().trim() : null);

        Maintenance saved = maintenanceRepository.save(maintenance);

        // Update vehicle status to MAINTENANCE if scheduled or in-progress
        if (saved.getStatus() != MaintenanceStatus.COMPLETED) {
            vehicle.setStatus(VehicleStatus.MAINTENANCE);
            vehicleRepository.save(vehicle);
        }

        return saved;
    }

    @Override
    public Maintenance updateMaintenance(Long id, MaintenanceRequest request) {
        Maintenance maintenance = getMaintenanceById(id);

        maintenance.setDescription(request.getDescription().trim());
        maintenance.setMaintenanceDate(request.getMaintenanceDate());
        maintenance.setCost(request.getCost());
        maintenance.setServiceProvider(request.getServiceProvider() != null ? request.getServiceProvider().trim() : null);
        maintenance.setNotes(request.getNotes() != null ? request.getNotes().trim() : null);

        if (request.getStatus() != null) {
            maintenance.setStatus(request.getStatus());
            if (request.getStatus() == MaintenanceStatus.COMPLETED && maintenance.getCompletionDate() == null) {
                maintenance.setCompletionDate(LocalDate.now());
            }
        }

        Maintenance saved = maintenanceRepository.save(maintenance);
        updateVehicleStatusAfterMaintenance(maintenance.getVehicle());
        return saved;
    }

    @Override
    public Maintenance updateStatus(Long id, MaintenanceStatus newStatus) {
        Maintenance maintenance = getMaintenanceById(id);
        maintenance.setStatus(newStatus);
        if (newStatus == MaintenanceStatus.COMPLETED) {
            maintenance.setCompletionDate(LocalDate.now());
        }

        Maintenance saved = maintenanceRepository.save(maintenance);
        updateVehicleStatusAfterMaintenance(maintenance.getVehicle());
        return saved;
    }

    private void updateVehicleStatusAfterMaintenance(Vehicle vehicle) {
        List<Maintenance> activeRecords = maintenanceRepository.findByVehicleIdOrderByMaintenanceDateDesc(vehicle.getId())
                .stream()
                .filter(m -> m.getStatus() == MaintenanceStatus.IN_PROGRESS || m.getStatus() == MaintenanceStatus.SCHEDULED)
                .toList();

        if (activeRecords.isEmpty() && vehicle.getStatus() == VehicleStatus.MAINTENANCE) {
            vehicle.setStatus(VehicleStatus.AVAILABLE);
            vehicleRepository.save(vehicle);
        } else if (!activeRecords.isEmpty()) {
            vehicle.setStatus(VehicleStatus.MAINTENANCE);
            vehicleRepository.save(vehicle);
        }
    }

    @Override
    public List<Maintenance> getAllMaintenance() {
        return maintenanceRepository.findAllByOrderByMaintenanceDateDesc();
    }

    @Override
    public List<Maintenance> getMaintenanceByVehicle(Long vehicleId) {
        return maintenanceRepository.findByVehicleIdOrderByMaintenanceDateDesc(vehicleId);
    }

    @Override
    public Maintenance getMaintenanceById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance record not found with id: " + id));
    }

    @Override
    public void deleteMaintenance(Long id) {
        Maintenance maintenance = getMaintenanceById(id);
        Vehicle vehicle = maintenance.getVehicle();
        maintenanceRepository.delete(maintenance);
        updateVehicleStatusAfterMaintenance(vehicle);
    }
}
