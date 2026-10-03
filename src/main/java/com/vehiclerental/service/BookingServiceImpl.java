package com.vehiclerental.service;

import com.vehiclerental.dto.BookingRequest;
import com.vehiclerental.dto.BookingResponseDto;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public BookingResponseDto createBooking(BookingRequest request, String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + customerEmail));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + request.getVehicleId()));

        if (vehicle.getStatus() == VehicleStatus.MAINTENANCE) {
            throw new BadRequestException("This vehicle is currently undergoing maintenance and is not available for booking.");
        }

        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();

        if (start.isBefore(LocalDate.now())) {
            throw new BadRequestException("Pickup date cannot be in the past.");
        }

        if (end.isBefore(start)) {
            throw new BadRequestException("Return date cannot be before pickup date.");
        }

        // Prevent overlapping bookings
        List<Booking> overlapping = bookingRepository.findOverlappingBookings(vehicle.getId(), start, end, null);
        if (!overlapping.isEmpty()) {
            Booking conflict = overlapping.get(0);
            throw new BadRequestException(String.format(
                    "Vehicle '%s %s' is already booked for the overlapping period (%s to %s). Please choose different dates.",
                    vehicle.getBrand(), vehicle.getModel(), conflict.getStartDate(), conflict.getEndDate()
            ));
        }

        int days = (int) ChronoUnit.DAYS.between(start, end);
        if (days == 0) {
            days = 1; // Same-day rental counts as 1 day
        }

        double totalAmount = days * vehicle.getPricePerDay();

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setVehicle(vehicle);
        booking.setStartDate(start);
        booking.setEndDate(end);
        booking.setTotalDays(days);
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.CONFIRMED); // Automatic confirmation or pending

        Booking saved = bookingRepository.save(booking);

        // Update vehicle status to BOOKED
        if (vehicle.getStatus() == VehicleStatus.AVAILABLE) {
            vehicle.setStatus(VehicleStatus.BOOKED);
            vehicleRepository.save(vehicle);
        }

        return enrichBookingDto(saved);
    }

    @Override
    public List<BookingResponseDto> getMyBookings(String customerEmail) {
        return bookingRepository.findByCustomerEmailOrderByCreatedAtDesc(customerEmail)
                .stream()
                .map(this::enrichBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getAllBookings() {
        return bookingRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::enrichBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        return enrichBookingDto(booking);
    }

    @Override
    public BookingResponseDto updateBookingStatus(Long id, BookingStatus newStatus, String reason) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        booking.setStatus(newStatus);
        if (reason != null && !reason.isBlank()) {
            booking.setCancellationReason(reason);
        }

        // Adjust vehicle status
        Vehicle vehicle = booking.getVehicle();
        if (newStatus == BookingStatus.CANCELLED || newStatus == BookingStatus.COMPLETED) {
            // Check if vehicle has any other active bookings
            List<Booking> active = bookingRepository.findOverlappingBookings(vehicle.getId(), LocalDate.now(), LocalDate.now().plusMonths(1), booking.getId());
            if (active.isEmpty() && vehicle.getStatus() != VehicleStatus.MAINTENANCE) {
                vehicle.setStatus(VehicleStatus.AVAILABLE);
                vehicleRepository.save(vehicle);
            }
        } else if (newStatus == BookingStatus.CONFIRMED) {
            if (vehicle.getStatus() == VehicleStatus.AVAILABLE) {
                vehicle.setStatus(VehicleStatus.BOOKED);
                vehicleRepository.save(vehicle);
            }
        }

        Booking saved = bookingRepository.save(booking);
        return enrichBookingDto(saved);
    }

    @Override
    public BookingResponseDto cancelBooking(Long id, String userEmail, String reason) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        // If not admin and not the customer who booked, forbid
        if (currentUser.getRole() != Role.ROLE_ADMIN && !booking.getCustomer().getEmail().equalsIgnoreCase(userEmail)) {
            throw new BadRequestException("You are not authorized to cancel this booking.");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BadRequestException("Completed bookings cannot be cancelled.");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("This booking is already cancelled.");
        }

        // Check if rental has already started
        Optional<Rental> rental = rentalRepository.findByBookingId(booking.getId());
        if (rental.isPresent() && rental.get().getRentalStatus() == RentalStatus.ACTIVE) {
            throw new BadRequestException("Cannot cancel booking after rental vehicle has been picked up. Please process a vehicle return.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(reason != null && !reason.isBlank() ? reason : "Cancelled by user");

        Vehicle vehicle = booking.getVehicle();
        if (vehicle.getStatus() == VehicleStatus.BOOKED) {
            vehicle.setStatus(VehicleStatus.AVAILABLE);
            vehicleRepository.save(vehicle);
        }

        Booking saved = bookingRepository.save(booking);
        return enrichBookingDto(saved);
    }

    private BookingResponseDto enrichBookingDto(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto(booking);

        // Check Rental
        Optional<Rental> rental = rentalRepository.findByBookingId(booking.getId());
        if (rental.isPresent()) {
            dto.setHasRental(true);
            dto.setRentalId(rental.get().getId());
            dto.setRentalStatus(rental.get().getRentalStatus().name());
        } else {
            dto.setHasRental(false);
        }

        // Check Payments
        List<Payment> payments = paymentRepository.findByBookingId(booking.getId());
        boolean isPaid = payments.stream().anyMatch(p -> p.getPaymentStatus() == PaymentStatus.SUCCESS);
        dto.setPaid(isPaid);
        if (!payments.isEmpty()) {
            dto.setPaymentStatus(payments.get(payments.size() - 1).getPaymentStatus().name());
        } else {
            dto.setPaymentStatus("UNPAID");
        }

        return dto;
    }
}
