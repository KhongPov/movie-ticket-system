package com.movieticket.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookingRequest {
    private Long showtimeId;
    private List<Long> seatIds; // IDs of seats to book
}
