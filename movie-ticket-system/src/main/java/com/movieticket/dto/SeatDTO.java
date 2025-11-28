package com.movieticket.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatDTO {
    private Long id;
    private String seatNumber;
    private String seatType;
    private String status;
}
