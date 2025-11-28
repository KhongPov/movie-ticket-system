package com.movieticket.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateReviewRequest {
    private Long movieId;
    private Integer rating; // 1-10
    private String comment;
}
