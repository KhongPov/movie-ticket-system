package com.movieticket.service;

import com.movieticket.dto.PaymentRequest;
import com.movieticket.dto.PaymentResponse;
import com.movieticket.entity.Booking;
import com.movieticket.entity.Payment;
import com.movieticket.repository.BookingRepository;
import com.movieticket.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // REMOVE Stripe API key injection completely

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {

        // Fake / Offline payment flow (Stripe disabled)
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check if payment already exists
        if (paymentRepository.findByBookingId(booking.getId()).isPresent()) {
            throw new RuntimeException("Payment already exists for this booking");
        }

        // Save fake payment
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalAmount())
                .paymentMethod(request.getPaymentMethod())
                .transactionId("OFFLINE-" + System.currentTimeMillis()) // Fake transaction ID
                .status("SUCCESS")
                .metadata("Stripe disabled - offline payment recorded")
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Update booking status
        booking.setStatus("COMPLETED");
        bookingRepository.save(booking);

        logger.info("Offline payment recorded for booking: {}", booking.getBookingReference());

        return PaymentResponse.builder()
                .paymentId(savedPayment.getId())
                .bookingReference(booking.getBookingReference())
                .amount(booking.getTotalAmount())
                .status("SUCCESS")
                .transactionId(savedPayment.getTransactionId())
                .message("Payment processed without Stripe (offline mode)")
                .build();
    }

    public PaymentResponse getPaymentStatus(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .bookingReference(payment.getBooking().getBookingReference())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .build();
    }

    public PaymentResponse getPaymentByBooking(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .bookingReference(payment.getBooking().getBookingReference())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .build();
    }

    @Transactional
    public PaymentResponse refundPayment(Long paymentId) {

        // Fake refund
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getStatus().equals("SUCCESS")) {
            throw new RuntimeException("Only successful payments can be refunded");
        }

        payment.setStatus("REFUNDED");
        payment.setMetadata("Refund processed offline");
        paymentRepository.save(payment);

        Booking booking = payment.getBooking();
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .bookingReference(booking.getBookingReference())
                .amount(payment.getAmount())
                .status("REFUNDED")
                .message("Refund processed without Stripe (offline mode)")
                .build();
    }
}
