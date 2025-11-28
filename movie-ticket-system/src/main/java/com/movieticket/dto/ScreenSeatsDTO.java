package com.movieticket.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScreenSeatsDTO {
    private Long screenId;
    private String screenNumber;
    private Integer totalSeats;
    private List<SeatDTO> seats;
}
