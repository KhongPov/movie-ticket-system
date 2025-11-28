package com.movieticket.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {
    private Long id;
    private String title;
    private String description;
    private String genre;
    private String language;
    private LocalDate releaseDate;
    private Integer duration;
    private Double rating;
    private String status;
    private String posterUrl;
}
