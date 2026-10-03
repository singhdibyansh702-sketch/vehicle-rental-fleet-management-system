package com.vehiclerental.controller;

import com.vehiclerental.dto.ApiResponse;
import com.vehiclerental.dto.PaymentRequest;
import com.vehiclerental.model.Payment;
import com.vehiclerental.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Payment>>> getAllPayments(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Payment> list = isAdmin ? paymentService.getAllPayments() : paymentService.getMyPayments(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("Payments retrieved", list));
    }

    @GetMapping("/my-payments")
    public ResponseEntity<ApiResponse<List<Payment>>> getMyPayments(Authentication authentication) {
        List<Payment> list = paymentService.getMyPayments(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("My payments retrieved", list));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentsByBooking(@PathVariable Long bookingId) {
        List<Payment> payments = paymentService.getPaymentsByBooking(bookingId);
        return ResponseEntity.ok(ApiResponse.ok("Booking payment records", payments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Payment>> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(ApiResponse.ok("Payment details", payment));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Payment>> processPayment(@Valid @RequestBody PaymentRequest request) {
        Payment payment = paymentService.processPayment(request);
        return new ResponseEntity<>(ApiResponse.ok("Payment processed successfully (Simulated)", payment), HttpStatus.CREATED);
    }
}
