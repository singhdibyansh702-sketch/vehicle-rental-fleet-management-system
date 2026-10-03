package com.vehiclerental.service;

import com.vehiclerental.dto.DashboardStatsDto;
import com.vehiclerental.model.*;
import com.vehiclerental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Override
    public DashboardStatsDto getAdminStats() {
        DashboardStatsDto stats = new DashboardStatsDto();

        stats.setTotalVehicles(vehicleRepository.count());
        stats.setAvailableVehicles(vehicleRepository.countByStatus(VehicleStatus.AVAILABLE));
        stats.setRentedVehicles(vehicleRepository.countByStatus(VehicleStatus.RENTED));
        stats.setMaintenanceVehicles(vehicleRepository.countByStatus(VehicleStatus.MAINTENANCE));
        stats.setBookedVehicles(vehicleRepository.countByStatus(VehicleStatus.BOOKED));

        stats.setTotalBookings(bookingRepository.count());
        stats.setPendingBookings(bookingRepository.countByStatus(BookingStatus.PENDING));
        stats.setConfirmedBookings(bookingRepository.countByStatus(BookingStatus.CONFIRMED));
        stats.setCompletedBookings(bookingRepository.countByStatus(BookingStatus.COMPLETED));
        stats.setCancelledBookings(bookingRepository.countByStatus(BookingStatus.CANCELLED));

        stats.setActiveRentals(rentalRepository.countByRentalStatus(RentalStatus.ACTIVE));
        stats.setTotalCustomers(userRepository.countByRole(Role.ROLE_CUSTOMER));

        stats.setTotalRevenue(paymentRepository.getTotalSuccessfulRevenue());
        stats.setTotalMaintenanceCost(maintenanceRepository.getTotalMaintenanceCost());

        return stats;
    }

    @Override
    public Map<String, Object> getCustomerStats(String customerEmail) {
        Map<String, Object> stats = new HashMap<>();

        List<Booking> bookings = bookingRepository.findByCustomerEmailOrderByCreatedAtDesc(customerEmail);
        long totalBookings = bookings.size();

        long activeRentalsCount = rentalRepository.findByBookingCustomerEmailOrderByCreatedAtDesc(customerEmail)
                .stream()
                .filter(r -> r.getRentalStatus() == RentalStatus.ACTIVE)
                .count();

        long upcomingBookings = bookings.stream()
                .filter(b -> (b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.PENDING)
                        && !b.getStartDate().isBefore(LocalDate.now()))
                .count();

        Double totalSpent = paymentRepository.getTotalSpentByCustomer(customerEmail);

        stats.put("totalBookings", totalBookings);
        stats.put("activeRentals", activeRentalsCount);
        stats.put("upcomingBookings", upcomingBookings);
        stats.put("totalSpent", totalSpent != null ? totalSpent : 0.0);

        return stats;
    }
}
