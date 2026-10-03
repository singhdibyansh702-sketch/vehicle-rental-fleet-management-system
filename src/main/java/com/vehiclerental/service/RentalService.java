package com.vehiclerental.service;

import com.vehiclerental.dto.RentalRequest;
import com.vehiclerental.dto.RentalReturnRequest;
import com.vehiclerental.model.Rental;

import java.util.List;

public interface RentalService {
    Rental createRental(RentalRequest request);
    Rental returnRental(Long rentalId, RentalReturnRequest request);
    List<Rental> getAllRentals();
    List<Rental> getActiveRentals();
    Rental getRentalById(Long id);
    Rental getRentalByBookingId(Long bookingId);
    List<Rental> getMyRentals(String customerEmail);
}
