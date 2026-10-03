package com.vehiclerental.dto;

import com.vehiclerental.model.MaintenanceStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class MaintenanceRequest {

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Maintenance date is required")
    private LocalDate maintenanceDate;

    @NotNull(message = "Cost is required")
    @Min(value = 0, message = "Cost cannot be negative")
    private Double cost;

    private MaintenanceStatus status = MaintenanceStatus.SCHEDULED;
    private String serviceProvider;
    private String notes;

    public MaintenanceRequest() {
    }

    public MaintenanceRequest(Long vehicleId, String description, LocalDate maintenanceDate,
                              Double cost, MaintenanceStatus status, String serviceProvider, String notes) {
        this.vehicleId = vehicleId;
        this.description = description;
        this.maintenanceDate = maintenanceDate;
        this.cost = cost;
        this.status = status != null ? status : MaintenanceStatus.SCHEDULED;
        this.serviceProvider = serviceProvider;
        this.notes = notes;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(LocalDate maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public MaintenanceStatus getStatus() {
        return status;
    }

    public void setStatus(MaintenanceStatus status) {
        this.status = status;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
