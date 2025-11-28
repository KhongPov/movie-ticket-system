package com.movieticket.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMovieRequest {
    private String title;
    private String description;
    private String genre;
    private String language;
    private LocalDate releaseDate;
    private Integer duration;
    private String posterUrl;
}
