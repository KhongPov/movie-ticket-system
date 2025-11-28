package com.movieticket.repository;

import com.movieticket.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    List<Showtime> findByMovieId(Long movieId);
    List<Showtime> findByTheaterId(Long theaterId);
    
    @Query("SELECT s FROM Showtime s WHERE s.movie.id = :movieId AND s.showDateTime >= :startDate")
    List<Showtime> findUpcomingShowtimes(@Param("movieId") Long movieId, 
                                         @Param("startDate") LocalDateTime startDate);

    @Query("SELECT s FROM Showtime s WHERE s.theater.id = :theaterId AND " +
           "s.showDateTime BETWEEN :startDate AND :endDate")
    List<Showtime> findShowtimesByTheaterAndDateRange(@Param("theaterId") Long theaterId,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);
}
