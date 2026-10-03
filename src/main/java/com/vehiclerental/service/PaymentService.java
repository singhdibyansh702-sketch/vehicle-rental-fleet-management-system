package com.vehiclerental.service;

import com.vehiclerental.dto.PaymentRequest;
import com.vehiclerental.model.Payment;

import java.util.List;

public interface PaymentService {
    Payment processPayment(PaymentRequest request);
    List<Payment> getPaymentsByBooking(Long bookingId);
    List<Payment> getAllPayments();
    List<Payment> getMyPayments(String customerEmail);
    Payment getPaymentById(Long id);
}
