package com.movieticket.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Long paymentId;
    private String bookingReference;
    private Double amount;
    private String status;
    private String transactionId;
    private String message;
}
