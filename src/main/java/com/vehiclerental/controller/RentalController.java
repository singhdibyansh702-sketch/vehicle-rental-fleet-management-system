package com.vehiclerental.controller;

import com.vehiclerental.dto.ApiResponse;
import com.vehiclerental.dto.RentalRequest;
import com.vehiclerental.dto.RentalReturnRequest;
import com.vehiclerental.model.Rental;
import com.vehiclerental.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Rental>>> getAllRentals(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Rental> list = isAdmin ? rentalService.getAllRentals() : rentalService.getMyRentals(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("Rentals retrieved", list));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<Rental>>> getActiveRentals() {
        List<Rental> list = rentalService.getActiveRentals();
        return ResponseEntity.ok(ApiResponse.ok("Active rentals retrieved", list));
    }

    @GetMapping("/my-rentals")
    public ResponseEntity<ApiResponse<List<Rental>>> getMyRentals(Authentication authentication) {
        List<Rental> list = rentalService.getMyRentals(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("My rentals retrieved", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Rental>> getRentalById(@PathVariable Long id) {
        Rental rental = rentalService.getRentalById(id);
        return ResponseEntity.ok(ApiResponse.ok("Rental details", rental));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<Rental>> getRentalByBookingId(@PathVariable Long bookingId) {
        Rental rental = rentalService.getRentalByBookingId(bookingId);
        return ResponseEntity.ok(ApiResponse.ok("Rental for booking", rental));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Rental>> startRental(@Valid @RequestBody RentalRequest request) {
        Rental rental = rentalService.createRental(request);
        return new ResponseEntity<>(ApiResponse.ok("Vehicle rental dispatched successfully", rental), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/return")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Rental>> processReturn(
            @PathVariable Long id,
            @Valid @RequestBody RentalReturnRequest request
    ) {
        Rental rental = rentalService.returnRental(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Vehicle return processed successfully", rental));
    }
}
