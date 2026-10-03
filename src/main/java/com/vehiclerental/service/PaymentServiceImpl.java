package com.vehiclerental.service;

import com.vehiclerental.dto.PaymentRequest;
import com.vehiclerental.exception.BadRequestException;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.model.Booking;
import com.vehiclerental.model.BookingStatus;
import com.vehiclerental.model.Payment;
import com.vehiclerental.model.PaymentStatus;
import com.vehiclerental.repository.BookingRepository;
import com.vehiclerental.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Payment processPayment(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + request.getBookingId()));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Cannot make payment for a cancelled booking.");
        }

        // Generate clean simulated transaction ID
        String prefix = request.getPaymentMethod().name();
        String txnId = "TXN-" + prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.SUCCESS); // Simulated instant success
        payment.setTransactionId(txnId);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setNotes(request.getNotes() != null ? request.getNotes().trim() : "Simulated " + prefix + " payment received");

        Payment saved = paymentRepository.save(payment);

        return saved;
    }

    @Override
    public List<Payment> getPaymentsByBooking(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAllByOrderByPaymentDateDesc();
    }

    @Override
    public List<Payment> getMyPayments(String customerEmail) {
        return paymentRepository.findByBookingCustomerEmailOrderByPaymentDateDesc(customerEmail);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
    }
}
