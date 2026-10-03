package com.vehiclerental.repository;

import com.vehiclerental.model.Booking;
import com.vehiclerental.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    List<Booking> findByCustomerEmailOrderByCreatedAtDesc(String email);

    List<Booking> findByVehicleIdOrderByCreatedAtDesc(Long vehicleId);

    List<Booking> findAllByOrderByCreatedAtDesc();

    long countByStatus(BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.vehicle.id = :vehicleId " +
           "AND b.status IN (com.vehiclerental.model.BookingStatus.PENDING, com.vehiclerental.model.BookingStatus.CONFIRMED) " +
           "AND b.startDate <= :endDate AND b.endDate >= :startDate " +
           "AND (:excludeBookingId IS NULL OR b.id != :excludeBookingId)")
    List<Booking> findOverlappingBookings(
            @Param("vehicleId") Long vehicleId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("excludeBookingId") Long excludeBookingId
    );
}
