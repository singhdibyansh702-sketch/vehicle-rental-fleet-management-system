package com.vehiclerental.dto;

public class DashboardStatsDto {
    private long totalVehicles;
    private long availableVehicles;
    private long rentedVehicles;
    private long maintenanceVehicles;
    private long bookedVehicles;

    private long totalBookings;
    private long pendingBookings;
    private long confirmedBookings;
    private long completedBookings;
    private long cancelledBookings;

    private long activeRentals;
    private long totalCustomers;

    private Double totalRevenue;
    private Double totalMaintenanceCost;

    public DashboardStatsDto() {
    }

    // Getters and Setters
    public long getTotalVehicles() {
        return totalVehicles;
    }

    public void setTotalVehicles(long totalVehicles) {
        this.totalVehicles = totalVehicles;
    }

    public long getAvailableVehicles() {
        return availableVehicles;
    }

    public void setAvailableVehicles(long availableVehicles) {
        this.availableVehicles = availableVehicles;
    }

    public long getRentedVehicles() {
        return rentedVehicles;
    }

    public void setRentedVehicles(long rentedVehicles) {
        this.rentedVehicles = rentedVehicles;
    }

    public long getMaintenanceVehicles() {
        return maintenanceVehicles;
    }

    public void setMaintenanceVehicles(long maintenanceVehicles) {
        this.maintenanceVehicles = maintenanceVehicles;
    }

    public long getBookedVehicles() {
        return bookedVehicles;
    }

    public void setBookedVehicles(long bookedVehicles) {
        this.bookedVehicles = bookedVehicles;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public long getPendingBookings() {
        return pendingBookings;
    }

    public void setPendingBookings(long pendingBookings) {
        this.pendingBookings = pendingBookings;
    }

    public long getConfirmedBookings() {
        return confirmedBookings;
    }

    public void setConfirmedBookings(long confirmedBookings) {
        this.confirmedBookings = confirmedBookings;
    }

    public long getCompletedBookings() {
        return completedBookings;
    }

    public void setCompletedBookings(long completedBookings) {
        this.completedBookings = completedBookings;
    }

    public long getCancelledBookings() {
        return cancelledBookings;
    }

    public void setCancelledBookings(long cancelledBookings) {
        this.cancelledBookings = cancelledBookings;
    }

    public long getActiveRentals() {
        return activeRentals;
    }

    public void setActiveRentals(long activeRentals) {
        this.activeRentals = activeRentals;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getTotalMaintenanceCost() {
        return totalMaintenanceCost;
    }

    public void setTotalMaintenanceCost(Double totalMaintenanceCost) {
        this.totalMaintenanceCost = totalMaintenanceCost;
    }
}
