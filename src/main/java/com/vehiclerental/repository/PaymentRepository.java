package com.vehiclerental.repository;

import com.vehiclerental.model.Payment;
import com.vehiclerental.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByBookingId(Long bookingId);

    List<Payment> findByBookingCustomerEmailOrderByPaymentDateDesc(String email);

    List<Payment> findAllByOrderByPaymentDateDesc();

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    @Query("SELECT COALESCE(SUM(p.amount), 0.0) FROM Payment p WHERE p.paymentStatus = com.vehiclerental.model.PaymentStatus.SUCCESS")
    Double getTotalSuccessfulRevenue();

    @Query("SELECT COALESCE(SUM(p.amount), 0.0) FROM Payment p WHERE p.paymentStatus = com.vehiclerental.model.PaymentStatus.SUCCESS AND p.booking.customer.email = :email")
    Double getTotalSpentByCustomer(String email);
}
