package com.vehiclerental.service;

import com.vehiclerental.dto.RentalRequest;
import com.vehiclerental.dto.RentalReturnRequest;
import com.vehiclerental.exception.BadRequestException;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.model.*;
import com.vehiclerental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RentalServiceImpl implements RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Override
    public Rental createRental(RentalRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + request.getBookingId()));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Cannot start rental for a cancelled booking.");
        }
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BadRequestException("This booking is already completed.");
        }

        Optional<Rental> existing = rentalRepository.findByBookingId(booking.getId());
        if (existing.isPresent()) {
            throw new BadRequestException("A rental record already exists for this booking.");
        }

        Rental rental = new Rental();
        rental.setBooking(booking);
        rental.setPickupDate(LocalDate.now());
        rental.setExpectedReturnDate(booking.getEndDate());
        rental.setRentalStatus(RentalStatus.ACTIVE);
        rental.setInitialOdometer(request.getInitialOdometer() != null ? request.getInitialOdometer() : 1000.0);
        rental.setAdditionalCharge(0.0);
        rental.setFinalTotalAmount(booking.getTotalAmount());

        Rental savedRental = rentalRepository.save(rental);

        // Update vehicle status to RENTED
        Vehicle vehicle = booking.getVehicle();
        vehicle.setStatus(VehicleStatus.RENTED);
        vehicleRepository.save(vehicle);

        // Ensure booking is marked confirmed
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        return savedRental;
    }

    @Override
    public Rental returnRental(Long rentalId, RentalReturnRequest request) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found with id: " + rentalId));

        if (rental.getRentalStatus() == RentalStatus.RETURNED) {
            throw new BadRequestException("This rental has already been processed for return.");
        }

        LocalDate actualReturn = request.getActualReturnDate() != null ? request.getActualReturnDate() : LocalDate.now();
        rental.setActualReturnDate(actualReturn);
        rental.setReturnOdometer(request.getReturnOdometer());
        rental.setFuelLevelReturn(request.getFuelLevelReturn());
        rental.setConditionNotes(request.getConditionNotes());
        rental.setRentalStatus(RentalStatus.RETURNED);

        // Calculate late fee if actual return is after expected return
        double calculatedLateFee = 0.0;
        if (actualReturn.isAfter(rental.getExpectedReturnDate())) {
            long lateDays = ChronoUnit.DAYS.between(rental.getExpectedReturnDate(), actualReturn);
            double dailyRate = rental.getBooking().getVehicle().getPricePerDay();
            // Late fee is 1.5x daily rate per overdue day
            calculatedLateFee = lateDays * dailyRate * 1.5;
        }

        double extraCharges = (request.getAdditionalCharge() != null ? request.getAdditionalCharge() : 0.0) + calculatedLateFee;
        rental.setAdditionalCharge(extraCharges);

        StringBuilder reasonBuilder = new StringBuilder();
        if (calculatedLateFee > 0) {
            reasonBuilder.append(String.format("Overdue late fee (₹%.0f). ", calculatedLateFee));
        }
        if (request.getAdditionalChargeReason() != null && !request.getAdditionalChargeReason().isBlank()) {
            reasonBuilder.append(request.getAdditionalChargeReason().trim());
        }
        rental.setAdditionalChargeReason(reasonBuilder.toString().trim());

        double finalTotal = rental.getBooking().getTotalAmount() + extraCharges;
        rental.setFinalTotalAmount(finalTotal);

        Rental savedRental = rentalRepository.save(rental);

        // Update booking to COMPLETED
        Booking booking = rental.getBooking();
        booking.setStatus(BookingStatus.COMPLETED);
        bookingRepository.save(booking);

        // Update vehicle status based on vehicle condition check
        Vehicle vehicle = booking.getVehicle();
        if ("NEEDS_MAINTENANCE".equalsIgnoreCase(request.getVehicleCondition())) {
            vehicle.setStatus(VehicleStatus.MAINTENANCE);
            vehicleRepository.save(vehicle);

            // Auto-create maintenance record
            Maintenance maintenance = new Maintenance(
                    vehicle,
                    "Post-rental maintenance check: " + (request.getConditionNotes() != null ? request.getConditionNotes() : "Inspection required"),
                    LocalDate.now(),
                    extraCharges > 0 ? extraCharges : 1000.0,
                    MaintenanceStatus.IN_PROGRESS,
                    "In-house Fleet Maintenance Team",
                    "Created automatically upon rental return inspection"
            );
            maintenanceRepository.save(maintenance);
        } else {
            vehicle.setStatus(VehicleStatus.AVAILABLE);
            vehicleRepository.save(vehicle);
        }

        return savedRental;
    }

    @Override
    public List<Rental> getAllRentals() {
        return rentalRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<Rental> getActiveRentals() {
        return rentalRepository.findByRentalStatus(RentalStatus.ACTIVE);
    }

    @Override
    public Rental getRentalById(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found with id: " + id));
    }

    @Override
    public Rental getRentalByBookingId(Long bookingId) {
        return rentalRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found for booking id: " + bookingId));
    }

    @Override
    public List<Rental> getMyRentals(String customerEmail) {
        return rentalRepository.findByBookingCustomerEmailOrderByCreatedAtDesc(customerEmail);
    }
}
