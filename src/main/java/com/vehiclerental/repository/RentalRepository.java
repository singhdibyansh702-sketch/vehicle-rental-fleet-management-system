package com.vehiclerental.repository;

import com.vehiclerental.model.Rental;
import com.vehiclerental.model.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    Optional<Rental> findByBookingId(Long bookingId);

    List<Rental> findByRentalStatus(RentalStatus rentalStatus);

    List<Rental> findAllByOrderByCreatedAtDesc();

    List<Rental> findByBookingCustomerEmailOrderByCreatedAtDesc(String email);

    long countByRentalStatus(RentalStatus rentalStatus);
}
