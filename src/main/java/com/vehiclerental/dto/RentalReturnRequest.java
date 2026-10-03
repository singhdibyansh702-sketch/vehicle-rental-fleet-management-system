package com.vehiclerental.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class RentalReturnRequest {

    @NotNull(message = "Actual return date is required")
    private LocalDate actualReturnDate;

    private Double returnOdometer;
    private String fuelLevelReturn;
    private String conditionNotes;
    private Double additionalCharge = 0.0;
    private String additionalChargeReason;
    private String vehicleCondition = "GOOD"; // "GOOD" or "NEEDS_MAINTENANCE"

    public RentalReturnRequest() {
    }

    public RentalReturnRequest(LocalDate actualReturnDate, Double returnOdometer, String fuelLevelReturn,
                               String conditionNotes, Double additionalCharge, String additionalChargeReason,
                               String vehicleCondition) {
        this.actualReturnDate = actualReturnDate;
        this.returnOdometer = returnOdometer;
        this.fuelLevelReturn = fuelLevelReturn;
        this.conditionNotes = conditionNotes;
        this.additionalCharge = additionalCharge != null ? additionalCharge : 0.0;
        this.additionalChargeReason = additionalChargeReason;
        this.vehicleCondition = vehicleCondition != null ? vehicleCondition : "GOOD";
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
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

    public String getVehicleCondition() {
        return vehicleCondition;
    }

    public void setVehicleCondition(String vehicleCondition) {
        this.vehicleCondition = vehicleCondition;
    }
}
