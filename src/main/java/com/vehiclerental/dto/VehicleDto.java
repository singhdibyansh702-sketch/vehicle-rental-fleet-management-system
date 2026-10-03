package com.vehiclerental.dto;

import com.vehiclerental.model.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VehicleDto {
    private Long id;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;

    @NotNull(message = "Fuel type is required")
    private FuelType fuelType;

    @NotNull(message = "Transmission is required")
    private Transmission transmission;

    @NotNull(message = "Seating capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer seatingCapacity;

    @NotNull(message = "Price per day is required")
    @Min(value = 0, message = "Price cannot be negative")
    private Double pricePerDay;

    private VehicleStatus status = VehicleStatus.AVAILABLE;
    private String imageUrl;
    private String description;
    private Integer year;
    private String mileage;

    public VehicleDto() {
    }

    public VehicleDto(Vehicle vehicle) {
        if (vehicle != null) {
            this.id = vehicle.getId();
            this.registrationNumber = vehicle.getRegistrationNumber();
            this.brand = vehicle.getBrand();
            this.model = vehicle.getModel();
            this.vehicleType = vehicle.getVehicleType();
            this.fuelType = vehicle.getFuelType();
            this.transmission = vehicle.getTransmission();
            this.seatingCapacity = vehicle.getSeatingCapacity();
            this.pricePerDay = vehicle.getPricePerDay();
            this.status = vehicle.getStatus();
            this.imageUrl = vehicle.getImageUrl();
            this.description = vehicle.getDescription();
            this.year = vehicle.getYear();
            this.mileage = vehicle.getMileage();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public Integer getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(Integer seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }
}
