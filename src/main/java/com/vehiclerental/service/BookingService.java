package com.vehiclerental.service;

import com.vehiclerental.dto.BookingRequest;
import com.vehiclerental.dto.BookingResponseDto;
import com.vehiclerental.model.BookingStatus;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(BookingRequest request, String customerEmail);
    List<BookingResponseDto> getMyBookings(String customerEmail);
    List<BookingResponseDto> getAllBookings();
    BookingResponseDto getBookingById(Long id);
    BookingResponseDto updateBookingStatus(Long id, BookingStatus status, String reason);
    BookingResponseDto cancelBooking(Long id, String userEmail, String reason);
}
