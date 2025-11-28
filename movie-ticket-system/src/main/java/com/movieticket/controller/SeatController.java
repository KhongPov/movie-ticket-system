package com.movieticket.controller;

import com.movieticket.dto.ScreenSeatsDTO;
import com.movieticket.dto.SeatDTO;
import com.movieticket.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<ScreenSeatsDTO> getScreenSeats(@PathVariable Long screenId) {
        return ResponseEntity.ok(seatService.getScreenSeats(screenId));
    }

    @GetMapping("/screen/{screenId}/available")
    public ResponseEntity<List<SeatDTO>> getAvailableSeats(@PathVariable Long screenId) {
        return ResponseEntity.ok(seatService.getAvailableSeats(screenId));
    }

    @PostMapping("/screen/{screenId}/initialize")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> initializeScreenSeats(@PathVariable Long screenId,
                                                      @RequestParam Integer totalSeats) {
        seatService.initializeScreenSeats(screenId, totalSeats);
        return ResponseEntity.noContent().build();
    }
}
