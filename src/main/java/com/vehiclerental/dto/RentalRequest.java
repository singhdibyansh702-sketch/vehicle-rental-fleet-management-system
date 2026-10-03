package com.vehiclerental.dto;

import jakarta.validation.constraints.NotNull;

public class RentalRequest {

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    private Double initialOdometer;

    public RentalRequest() {
    }

    public RentalRequest(Long bookingId, Double initialOdometer) {
        this.bookingId = bookingId;
        this.initialOdometer = initialOdometer;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Double getInitialOdometer() {
        return initialOdometer;
    }

    public void setInitialOdometer(Double initialOdometer) {
        this.initialOdometer = initialOdometer;
    }
}
