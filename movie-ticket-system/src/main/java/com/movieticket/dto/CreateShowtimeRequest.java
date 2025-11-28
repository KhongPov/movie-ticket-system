package com.movieticket.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateShowtimeRequest {
    private Long movieId;
    private Long theaterId;
    private Long screenId;
    private LocalDateTime showDateTime;
    private Double ticketPrice;
}
