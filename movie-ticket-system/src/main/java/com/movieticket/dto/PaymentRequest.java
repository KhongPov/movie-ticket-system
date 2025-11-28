package com.movieticket.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private Long bookingId;
    private String paymentMethod; // CREDIT_CARD, DEBIT_CARD, etc.
    private String stripeToken; // Token from Stripe
}
