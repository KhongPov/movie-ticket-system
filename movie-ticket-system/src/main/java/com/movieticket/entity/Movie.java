package com.movieticket.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Column
    private Integer duration; // in minutes

    @Column(columnDefinition = "DECIMAL(3, 1) DEFAULT 0.0")
    private Double rating; // 0-10

    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'ACTIVE'")
    private String status; // ACTIVE, COMING_SOON, ENDED

    @Column(columnDefinition = "TEXT")
    private String posterUrl;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<Showtime> showtimes = new HashSet<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
