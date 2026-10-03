package com.vehiclerental.dto;

import com.vehiclerental.model.Booking;
import com.vehiclerental.model.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingResponseDto {
    private Long id;
    private String bookingNumber;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private Long vehicleId;
    private String vehicleBrand;
    private String vehicleModel;
    private String vehicleRegistrationNumber;
    private String vehicleType;
    private String vehicleImageUrl;
    private Double pricePerDay;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalDays;
    private Double totalAmount;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private String cancellationReason;
    private boolean hasRental;
    private Long rentalId;
    private String rentalStatus;
    private boolean isPaid;
    private String paymentStatus;

    public BookingResponseDto() {
    }

    public BookingResponseDto(Booking booking) {
        if (booking != null) {
            this.id = booking.getId();
            this.bookingNumber = booking.getBookingNumber();
            if (booking.getCustomer() != null) {
                this.customerId = booking.getCustomer().getId();
                this.customerName = booking.getCustomer().getName();
                this.customerEmail = booking.getCustomer().getEmail();
                this.customerPhone = booking.getCustomer().getPhone();
            }
            if (booking.getVehicle() != null) {
                this.vehicleId = booking.getVehicle().getId();
                this.vehicleBrand = booking.getVehicle().getBrand();
                this.vehicleModel = booking.getVehicle().getModel();
                this.vehicleRegistrationNumber = booking.getVehicle().getRegistrationNumber();
                this.vehicleType = booking.getVehicle().getVehicleType() != null ? booking.getVehicle().getVehicleType().name() : null;
                this.vehicleImageUrl = booking.getVehicle().getImageUrl();
                this.pricePerDay = booking.getVehicle().getPricePerDay();
            }
            this.startDate = booking.getStartDate();
            this.endDate = booking.getEndDate();
            this.totalDays = booking.getTotalDays();
            this.totalAmount = booking.getTotalAmount();
            this.status = booking.getStatus();
            this.createdAt = booking.getCreatedAt();
            this.cancellationReason = booking.getCancellationReason();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleImageUrl() {
        return vehicleImageUrl;
    }

    public void setVehicleImageUrl(String vehicleImageUrl) {
        this.vehicleImageUrl = vehicleImageUrl;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public boolean isHasRental() {
        return hasRental;
    }

    public void setHasRental(boolean hasRental) {
        this.hasRental = hasRental;
    }

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    public String getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(String rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
