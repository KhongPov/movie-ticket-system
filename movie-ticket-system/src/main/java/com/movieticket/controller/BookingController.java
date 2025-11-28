package com.movieticket.controller;

import com.movieticket.dto.BookingDTO;
import com.movieticket.dto.CreateBookingRequest;
import com.movieticket.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private com.movieticket.repository.UserRepository userRepository;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingDTO> createBooking(@RequestBody CreateBookingRequest request,
                                                    Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        com.movieticket.entity.User user = userRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        BookingDTO booking = bookingService.createBooking(user.getId(), request);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/reference/{reference}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingDTO> getBookingByReference(@PathVariable String reference) {
        return ResponseEntity.ok(bookingService.getBookingByReference(reference));
    }

    @GetMapping("/user/my-bookings")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookingDTO>> getMyBookings(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        com.movieticket.entity.User user = userRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return ResponseEntity.ok(bookingService.getBookingsByUser(user.getId()));
    }

    @GetMapping("/user/upcoming")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookingDTO>> getUpcomingBookings(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        com.movieticket.entity.User user = userRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return ResponseEntity.ok(bookingService.getUpcomingBookingsByUser(user.getId()));
    }

    @GetMapping("/user/completed")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookingDTO>> getCompletedBookings(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        com.movieticket.entity.User user = userRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return ResponseEntity.ok(bookingService.getCompletedBookingsByUser(user.getId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}
