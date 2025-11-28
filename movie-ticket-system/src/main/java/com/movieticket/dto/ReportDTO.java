package com.movieticket.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
    private Long totalBookings;
    private Long totalRevenue;
    private Long totalUsers;
    private Long totalMovies;
    private Double averageBookingValue;
    private LocalDate reportDate;
    private Long completedBookings;
    private Long cancelledBookings;
    private Long activeMovies;
}
