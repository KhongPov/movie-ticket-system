package com.movieticket.controller;

import com.movieticket.dto.CreateShowtimeRequest;
import com.movieticket.dto.ShowtimeDTO;
import com.movieticket.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/showtimes")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ShowtimeController {

    @Autowired
    private ShowtimeService showtimeService;

    @GetMapping("/{id}")
    public ResponseEntity<ShowtimeDTO> getShowtimeById(@PathVariable Long id) {
        return ResponseEntity.ok(showtimeService.getShowtimeById(id));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowtimeDTO>> getShowtimesByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showtimeService.getShowtimesByMovie(movieId));
    }

    @GetMapping("/movie/{movieId}/upcoming")
    public ResponseEntity<List<ShowtimeDTO>> getUpcomingShowtimes(@PathVariable Long movieId) {
        return ResponseEntity.ok(showtimeService.getUpcomingShowtimes(movieId));
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<ShowtimeDTO>> getShowtimesByTheater(@PathVariable Long theaterId) {
        return ResponseEntity.ok(showtimeService.getShowtimesByTheater(theaterId));
    }

    @GetMapping("/{showtimeId}/available-seats")
    public ResponseEntity<Integer> getAvailableSeats(@PathVariable Long showtimeId) {
        return ResponseEntity.ok(showtimeService.getAvailableSeats(showtimeId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowtimeDTO> createShowtime(@RequestBody CreateShowtimeRequest request) {
        return ResponseEntity.ok(showtimeService.createShowtime(request));
    }

    @PutMapping("/{id}/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateShowtimeStatus(@PathVariable Long id, @PathVariable String status) {
        showtimeService.updateShowtimeStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cancelShowtime(@PathVariable Long id) {
        showtimeService.cancelShowtime(id);
        return ResponseEntity.noContent().build();
    }
}
