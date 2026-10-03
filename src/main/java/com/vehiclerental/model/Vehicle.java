package com.vehiclerental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Registration number is required")
    @Column(name = "registration_number", nullable = false, unique = true, length = 30)
    private String registrationNumber;

    @NotBlank(message = "Brand is required")
    @Column(nullable = false, length = 60)
    private String brand;

    @NotBlank(message = "Model is required")
    @Column(nullable = false, length = 60)
    private String model;

    @NotNull(message = "Vehicle type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 30)
    private VehicleType vehicleType;

    @NotNull(message = "Fuel type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false, length = 30)
    private FuelType fuelType;

    @NotNull(message = "Transmission is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Transmission transmission;

    @NotNull(message = "Seating capacity is required")
    @Min(value = 1, message = "Seating capacity must be at least 1")
    @Column(name = "seating_capacity", nullable = false)
    private Integer seatingCapacity;

    @NotNull(message = "Price per day is required")
    @Min(value = 0, message = "Price per day cannot be negative")
    @Column(name = "price_per_day", nullable = false)
    private Double pricePerDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private VehicleStatus status = VehicleStatus.AVAILABLE;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(length = 1000)
    private String description;

    @Column(name = "manufacture_year")
    private Integer year;

    @Column(length = 50)
    private String mileage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Vehicle() {
    }

    public Vehicle(String registrationNumber, String brand, String model, VehicleType vehicleType,
                   FuelType fuelType, Transmission transmission, Integer seatingCapacity,
                   Double pricePerDay, VehicleStatus status, String imageUrl, String description,
                   Integer year, String mileage) {
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.model = model;
        this.vehicleType = vehicleType;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.seatingCapacity = seatingCapacity;
        this.pricePerDay = pricePerDay;
        this.status = status != null ? status : VehicleStatus.AVAILABLE;
        this.imageUrl = imageUrl;
        this.description = description;
        this.year = year;
        this.mileage = mileage;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = VehicleStatus.AVAILABLE;
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
        this.registrationNumber = registrationNumber != null ? registrationNumber.trim().toUpperCase() : null;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
