package com.movieticket.repository;

import com.movieticket.entity.BookingTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingTicketRepository extends JpaRepository<BookingTicket, Long> {
    List<BookingTicket> findByBookingId(Long bookingId);
}
