package com.movieticket.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowtimeDTO {
    private Long id;
    private Long movieId;
    private Long theaterId;
    private Long screenId;
    private LocalDateTime showDateTime;
    private Double ticketPrice;
    private String status;
    private String movieTitle;
    private String theaterName;
    private String screenNumber;
    private Integer availableSeats;
}
