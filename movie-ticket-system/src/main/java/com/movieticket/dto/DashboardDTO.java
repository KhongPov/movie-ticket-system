package com.movieticket.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {
    private Long totalUsers;
    private Long totalMovies;
    private Long totalShowtimes;
    private Long totalBookings;
    private Long totalRevenue;
    private Long completedBookings;
    private Long cancelledBookings;
    private Double averageBookingValue;
    private Long totalTheaters;
    private Long totalScreens;
}
