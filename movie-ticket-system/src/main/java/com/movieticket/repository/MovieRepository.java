package com.movieticket.repository;

import com.movieticket.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByStatus(String status);
    List<Movie> findByGenre(String genre);
    
    @Query("SELECT m FROM Movie m WHERE m.status = 'ACTIVE' AND m.language = :language")
    List<Movie> findByLanguage(@Param("language") String language);

    @Query("SELECT m FROM Movie m WHERE m.status = 'ACTIVE' AND " +
           "(LOWER(m.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.genre) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Movie> searchMovies(@Param("searchTerm") String searchTerm);

    List<Movie> findByReleaseDateAfterAndStatus(LocalDate date, String status);
}
