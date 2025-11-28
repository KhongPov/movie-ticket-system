package com.movieticket.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {
    private Long id;
    private String bookingReference;
    private Long showtimeId;
    private String movieTitle;
    private String theaterName;
    private LocalDateTime showDateTime;
    private Integer numberOfTickets;
    private Double totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private Set<BookingTicketDTO> tickets;
}
