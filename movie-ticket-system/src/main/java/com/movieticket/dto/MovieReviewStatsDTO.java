package com.movieticket.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieReviewStatsDTO {
    private Long movieId;
    private String movieTitle;
    private Double averageRating;
    private Long totalReviews;
    private Long fiveStarCount;
    private Long fourStarCount;
    private Long threeStarCount;
    private Long twoStarCount;
    private Long oneStarCount;
}
