package com.movieticket.repository;

import com.movieticket.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMovieId(Long movieId);
    List<Review> findByUserId(Long userId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.movie.id = :movieId")
    Double getAverageRatingByMovie(@Param("movieId") Long movieId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.movie.id = :movieId")
    Long getReviewCountByMovie(@Param("movieId") Long movieId);

    @Query("SELECT r FROM Review r WHERE r.movie.id = :movieId ORDER BY r.createdAt DESC")
    List<Review> findRecentReviewsByMovie(@Param("movieId") Long movieId);
}
