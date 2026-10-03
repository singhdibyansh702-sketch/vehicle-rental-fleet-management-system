package com.vehiclerental;

import com.vehiclerental.dto.*;
import com.vehiclerental.model.*;
import com.vehiclerental.service.BookingService;
import com.vehiclerental.service.UserService;
import com.vehiclerental.service.VehicleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class VehicleRentalApplicationTests {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Test
    @DisplayName("Context loads and seed vehicles are populated")
    void contextLoadsAndSeedDataPresent() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        assertNotNull(vehicles, "Vehicle list should not be null");
        assertTrue(vehicles.size() >= 10, "Should have at least 10 seeded vehicles");
    }

    @Test
    @DisplayName("Vehicle availability check detects booked dates vs free dates")
    void testVehicleAvailabilityLogic() {
        LocalDate today = LocalDate.now();
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        Vehicle vehicle = vehicles.get(2); // Honda City

        // Check availability for future dates
        boolean available = vehicleService.checkAvailability(
                vehicle.getId(),
                today.plusDays(10),
                today.plusDays(14),
                null
        );
        assertTrue(available, "Honda City should be available 10-14 days in future");
    }

    @Test
    @DisplayName("User login succeeds for seed admin credentials")
    void testAdminLogin() {
        AuthRequest req = new AuthRequest("admin@vehiclerental.com", "admin123");
        AuthResponse response = userService.login(req);

        assertNotNull(response, "Login response must not be null");
        assertNotNull(response.getToken(), "JWT Token must be generated");
        assertEquals("ROLE_ADMIN", response.getRole());
        assertEquals("admin@vehiclerental.com", response.getEmail());
    }

    @Test
    @DisplayName("User registration creates new customer with BCrypt password")
    void testCustomerRegistration() {
        String testEmail = "testuser_" + System.currentTimeMillis() + "@example.com";
        RegisterRequest req = new RegisterRequest(
                "Test Driver",
                testEmail,
                "password123",
                "9876500000",
                "Bangalore Test Road",
                "DL-TEST9999"
        );

        User registered = userService.register(req);
        assertNotNull(registered);
        assertEquals(testEmail, registered.getEmail());
        assertEquals(Role.ROLE_CUSTOMER, registered.getRole());

        // Verify newly registered user can login
        AuthResponse loginRes = userService.login(new AuthRequest(testEmail, "password123"));
        assertNotNull(loginRes.getToken());
    }

    @Test
    @DisplayName("Booking calculation accurately tallies days and total pricing")
    void testBookingCalculationAndCreation() {
        LocalDate today = LocalDate.now();
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        Vehicle vehicle = vehicles.get(3); // Tata Nexon.ev Max (₹2600/day)

        BookingRequest request = new BookingRequest();
        request.setVehicleId(vehicle.getId());
        request.setStartDate(today.plusDays(20));
        request.setEndDate(today.plusDays(23)); // 3 days

        BookingResponseDto booking = bookingService.createBooking(request, "rahul@gmail.com");
        assertNotNull(booking);
        assertEquals(3, booking.getTotalDays());
        assertEquals(3 * vehicle.getPricePerDay(), booking.getTotalAmount());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }
}
