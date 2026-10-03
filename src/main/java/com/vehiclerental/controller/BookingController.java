package com.vehiclerental.controller;

import com.vehiclerental.dto.ApiResponse;
import com.vehiclerental.dto.BookingRequest;
import com.vehiclerental.dto.BookingResponseDto;
import com.vehiclerental.model.BookingStatus;
import com.vehiclerental.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponseDto>> createBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        BookingResponseDto booking = bookingService.createBooking(request, email);
        return new ResponseEntity<>(ApiResponse.ok("Booking confirmed successfully", booking), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingResponseDto>>> getBookings(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<BookingResponseDto> list;
        if (isAdmin) {
            list = bookingService.getAllBookings();
        } else {
            list = bookingService.getMyBookings(authentication.getName());
        }
        return ResponseEntity.ok(ApiResponse.ok("Bookings retrieved successfully", list));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<List<BookingResponseDto>>> getMyBookings(Authentication authentication) {
        List<BookingResponseDto> list = bookingService.getMyBookings(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("My bookings retrieved successfully", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponseDto>> getBookingById(@PathVariable Long id) {
        BookingResponseDto booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(ApiResponse.ok("Booking details", booking));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<BookingResponseDto>> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload
    ) {
        String statusStr = payload.get("status");
        String reason = payload.get("reason");
        BookingStatus status = BookingStatus.valueOf(statusStr.toUpperCase());
        BookingResponseDto updated = bookingService.updateBookingStatus(id, status, reason);
        return ResponseEntity.ok(ApiResponse.ok("Booking status updated to " + status, updated));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponseDto>> cancelBooking(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> payload,
            Authentication authentication
    ) {
        String reason = (payload != null && payload.containsKey("reason")) ? payload.get("reason") : "Cancelled by user";
        BookingResponseDto cancelled = bookingService.cancelBooking(id, authentication.getName(), reason);
        return ResponseEntity.ok(ApiResponse.ok("Booking cancelled successfully", cancelled));
    }
}
