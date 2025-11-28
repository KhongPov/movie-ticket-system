package com.movieticket.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingTicketDTO {
    private Long id;
    private String seatNumber;
    private String seatType;
    private Double price;
}
