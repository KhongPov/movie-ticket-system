package com.movieticket.service;

import com.movieticket.dto.BookingDTO;
import com.movieticket.dto.BookingTicketDTO;
import com.movieticket.dto.CreateBookingRequest;
import com.movieticket.entity.*;
import com.movieticket.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingTicketRepository bookingTicketRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final String BOOKING_PREFIX = "BK";

    @Transactional
    public BookingDTO createBooking(Long userId, CreateBookingRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        List<Seat> seats = seatRepository.findAllById(request.getSeatIds());

        if (seats.size() != request.getSeatIds().size()) {
            throw new RuntimeException("Some seats not found");
        }

        // Check if all seats are available and belong to the same screen
        for (Seat seat : seats) {
            if (!seat.getStatus().equals("AVAILABLE")) {
                throw new RuntimeException("Seat " + seat.getSeatNumber() + " is not available");
            }
            if (!seat.getScreen().getId().equals(showtime.getScreen().getId())) {
                throw new RuntimeException("Seat does not belong to the showtime's screen");
            }
        }

        // Create booking
        String bookingReference = generateBookingReference();
        Double totalAmount = showtime.getTicketPrice() * request.getSeatIds().size();

        Booking booking = Booking.builder()
                .bookingReference(bookingReference)
                .user(user)
                .showtime(showtime)
                .numberOfTickets(request.getSeatIds().size())
                .totalAmount(totalAmount)
                .status("CONFIRMED")
                .tickets(new HashSet<>())
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        // Create booking tickets and update seat status
        for (Seat seat : seats) {
            seat.setStatus("BOOKED");
            seatRepository.save(seat);

            BookingTicket ticket = BookingTicket.builder()
                    .booking(savedBooking)
                    .seat(seat)
                    .price(showtime.getTicketPrice())
                    .build();

            bookingTicketRepository.save(ticket);
            savedBooking.getTickets().add(ticket);
        }

        return mapToDTO(savedBooking);
    }

    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return mapToDTO(booking);
    }

    public BookingDTO getBookingByReference(String reference) {
        Booking booking = bookingRepository.findByBookingReference(reference)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return mapToDTO(booking);
    }

    public List<BookingDTO> getBookingsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookingRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getUpcomingBookingsByUser(Long userId) {
        return bookingRepository.findUpcomingBookingsByUser(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getCompletedBookingsByUser(Long userId) {
        return bookingRepository.findCompletedBookingsByUser(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getStatus().equals("CONFIRMED")) {
            throw new RuntimeException("Only confirmed bookings can be cancelled");
        }

        booking.setStatus("CANCELLED");

        // Release seats
        for (BookingTicket ticket : booking.getTickets()) {
            ticket.getSeat().setStatus("AVAILABLE");
            seatRepository.save(ticket.getSeat());
        }

        bookingRepository.save(booking);
    }

    private String generateBookingReference() {
        String timestamp = System.currentTimeMillis() / 1000 + "";
        String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return BOOKING_PREFIX + timestamp.substring(timestamp.length() - 6) + random;
    }

    private BookingDTO mapToDTO(Booking booking) {
        Set<BookingTicketDTO> ticketDTOs = booking.getTickets().stream()
                .map(ticket -> BookingTicketDTO.builder()
                        .id(ticket.getId())
                        .seatNumber(ticket.getSeat().getSeatNumber())
                        .seatType(ticket.getSeat().getSeatType())
                        .price(ticket.getPrice())
                        .build())
                .collect(Collectors.toSet());

        return BookingDTO.builder()
                .id(booking.getId())
                .bookingReference(booking.getBookingReference())
                .showtimeId(booking.getShowtime().getId())
                .movieTitle(booking.getShowtime().getMovie().getTitle())
                .theaterName(booking.getShowtime().getTheater().getName())
                .showDateTime(booking.getShowtime().getShowDateTime())
                .numberOfTickets(booking.getNumberOfTickets())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .tickets(ticketDTOs)
                .build();
    }
}
