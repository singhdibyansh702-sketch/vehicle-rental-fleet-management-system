package com.vehiclerental.service;

import com.vehiclerental.dto.VehicleDto;
import com.vehiclerental.exception.BadRequestException;
import com.vehiclerental.exception.DuplicateResourceException;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.model.*;
import com.vehiclerental.repository.BookingRepository;
import com.vehiclerental.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public List<Vehicle> filterVehicles(VehicleType type, FuelType fuel, Transmission transmission,
                                        VehicleStatus status, Double maxPrice, String keyword) {
        String cleanKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        return vehicleRepository.filterVehicles(type, fuel, transmission, status, maxPrice, cleanKeyword);
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
    }

    @Override
    public Vehicle createVehicle(VehicleDto dto) {
        String regNo = dto.getRegistrationNumber().trim().toUpperCase();
        if (vehicleRepository.existsByRegistrationNumber(regNo)) {
            throw new DuplicateResourceException("Vehicle with registration number '" + regNo + "' already exists");
        }

        Vehicle vehicle = new Vehicle();
        mapDtoToVehicle(dto, vehicle);
        vehicle.setRegistrationNumber(regNo);
        vehicle.setStatus(dto.getStatus() != null ? dto.getStatus() : VehicleStatus.AVAILABLE);

        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle updateVehicle(Long id, VehicleDto dto) {
        Vehicle vehicle = getVehicleById(id);
        String regNo = dto.getRegistrationNumber().trim().toUpperCase();

        if (!vehicle.getRegistrationNumber().equalsIgnoreCase(regNo) &&
                vehicleRepository.existsByRegistrationNumber(regNo)) {
            throw new DuplicateResourceException("Vehicle with registration number '" + regNo + "' already exists");
        }

        mapDtoToVehicle(dto, vehicle);
        vehicle.setRegistrationNumber(regNo);
        if (dto.getStatus() != null) {
            vehicle.setStatus(dto.getStatus());
        }

        return vehicleRepository.save(vehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicle vehicle = getVehicleById(id);
        // Check if there are active bookings or rentals
        List<Booking> bookings = bookingRepository.findByVehicleIdOrderByCreatedAtDesc(id);
        boolean hasActive = bookings.stream().anyMatch(b ->
                b.getStatus() == BookingStatus.PENDING || b.getStatus() == BookingStatus.CONFIRMED);
        if (hasActive) {
            throw new BadRequestException("Cannot delete vehicle because it has active/pending bookings");
        }
        vehicleRepository.delete(vehicle);
    }

    @Override
    public Vehicle updateVehicleStatus(Long id, VehicleStatus status) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setStatus(status);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public boolean checkAvailability(Long vehicleId, LocalDate startDate, LocalDate endDate, Long excludeBookingId) {
        Vehicle vehicle = getVehicleById(vehicleId);

        if (vehicle.getStatus() == VehicleStatus.MAINTENANCE) {
            return false;
        }

        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("Pickup date cannot be after return date");
        }

        List<Booking> overlapping = bookingRepository.findOverlappingBookings(vehicleId, startDate, endDate, excludeBookingId);
        return overlapping.isEmpty();
    }

    private void mapDtoToVehicle(VehicleDto dto, Vehicle vehicle) {
        vehicle.setBrand(dto.getBrand().trim());
        vehicle.setModel(dto.getModel().trim());
        vehicle.setVehicleType(dto.getVehicleType());
        vehicle.setFuelType(dto.getFuelType());
        vehicle.setTransmission(dto.getTransmission());
        vehicle.setSeatingCapacity(dto.getSeatingCapacity());
        vehicle.setPricePerDay(dto.getPricePerDay());
        vehicle.setImageUrl(dto.getImageUrl() != null ? dto.getImageUrl().trim() : null);
        vehicle.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        vehicle.setYear(dto.getYear());
        vehicle.setMileage(dto.getMileage() != null ? dto.getMileage().trim() : null);
    }
}
