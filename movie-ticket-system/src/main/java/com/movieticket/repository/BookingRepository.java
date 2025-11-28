package com.movieticket.repository;

import com.movieticket.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingReference(String reference);
    List<Booking> findByUserId(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.showtime.showDateTime > :now " +
           "ORDER BY b.showtime.showDateTime ASC")
    List<Booking> findUpcomingBookingsByUser(@Param("userId") Long userId, 
                                             @Param("now") LocalDateTime now);

    default List<Booking> findUpcomingBookingsByUser(Long userId) {
        return findUpcomingBookingsByUser(userId, LocalDateTime.now());
    }

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.showtime.showDateTime < :now " +
           "ORDER BY b.showtime.showDateTime DESC")
    List<Booking> findCompletedBookingsByUser(@Param("userId") Long userId,
                                              @Param("now") LocalDateTime now);

    default List<Booking> findCompletedBookingsByUser(Long userId) {
        return findCompletedBookingsByUser(userId, LocalDateTime.now());
    }

    @Query("SELECT b FROM Booking b WHERE b.showtime.id = :showtimeId")
    List<Booking> findByShowtimeId(@Param("showtimeId") Long showtimeId);
}
