package com.vehiclerental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rental_number", unique = true, length = 30)
    private String rentalNumber;

    @NotNull(message = "Booking is required")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @NotNull(message = "Pickup date is required")
    @Column(name = "pickup_date", nullable = false)
    private LocalDate pickupDate;

    @NotNull(message = "Expected return date is required")
    @Column(name = "expected_return_date", nullable = false)
    private LocalDate expectedReturnDate;

    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "rental_status", nullable = false, length = 30)
    private RentalStatus rentalStatus = RentalStatus.ACTIVE;

    @Column(name = "initial_odometer")
    private Double initialOdometer;

    @Column(name = "return_odometer")
    private Double returnOdometer;

    @Column(name = "fuel_level_return", length = 50)
    private String fuelLevelReturn;

    @Column(name = "condition_notes", length = 500)
    private String conditionNotes;

    @Column(name = "additional_charge")
    private Double additionalCharge = 0.0;

    @Column(name = "additional_charge_reason", length = 255)
    private String additionalChargeReason;

    @Column(name = "final_total_amount")
    private Double finalTotalAmount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Rental() {
    }

    public Rental(Booking booking, LocalDate pickupDate, LocalDate expectedReturnDate,
                  RentalStatus rentalStatus, Double initialOdometer) {
        this.booking = booking;
        this.pickupDate = pickupDate;
        this.expectedReturnDate = expectedReturnDate;
        this.rentalStatus = rentalStatus != null ? rentalStatus : RentalStatus.ACTIVE;
        this.initialOdometer = initialOdometer;
        this.additionalCharge = 0.0;
        this.finalTotalAmount = booking != null ? booking.getTotalAmount() : 0.0;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.rentalStatus == null) {
            this.rentalStatus = RentalStatus.ACTIVE;
        }
        if (this.rentalNumber == null) {
            this.rentalNumber = "RNT-" + System.currentTimeMillis() % 1000000;
        }
        if (this.additionalCharge == null) {
            this.additionalCharge = 0.0;
        }
        if (this.finalTotalAmount == null && this.booking != null) {
            this.finalTotalAmount = this.booking.getTotalAmount();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRentalNumber() {
        return rentalNumber;
    }

    public void setRentalNumber(String rentalNumber) {
        this.rentalNumber = rentalNumber;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public RentalStatus getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(RentalStatus rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public Double getInitialOdometer() {
        return initialOdometer;
    }

    public void setInitialOdometer(Double initialOdometer) {
        this.initialOdometer = initialOdometer;
    }

    public Double getReturnOdometer() {
        return returnOdometer;
    }

    public void setReturnOdometer(Double returnOdometer) {
        this.returnOdometer = returnOdometer;
    }

    public String getFuelLevelReturn() {
        return fuelLevelReturn;
    }

    public void setFuelLevelReturn(String fuelLevelReturn) {
        this.fuelLevelReturn = fuelLevelReturn;
    }

    public String getConditionNotes() {
        return conditionNotes;
    }

    public void setConditionNotes(String conditionNotes) {
        this.conditionNotes = conditionNotes;
    }

    public Double getAdditionalCharge() {
        return additionalCharge;
    }

    public void setAdditionalCharge(Double additionalCharge) {
        this.additionalCharge = additionalCharge;
    }

    public String getAdditionalChargeReason() {
        return additionalChargeReason;
    }

    public void setAdditionalChargeReason(String additionalChargeReason) {
        this.additionalChargeReason = additionalChargeReason;
    }

    public Double getFinalTotalAmount() {
        return finalTotalAmount;
    }

    public void setFinalTotalAmount(Double finalTotalAmount) {
        this.finalTotalAmount = finalTotalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
