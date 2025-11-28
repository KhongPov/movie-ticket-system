package com.movieticket.controller;

import com.movieticket.entity.Screen;
import com.movieticket.entity.Theater;
import com.movieticket.service.AdminTheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/theaters")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTheaterController {

    @Autowired
    private AdminTheaterService theaterService;

    @PostMapping
    public ResponseEntity<Theater> createTheater(@RequestBody Theater theater) {
        return ResponseEntity.ok(theaterService.createTheater(theater));
    }

    @GetMapping
    public ResponseEntity<List<Theater>> getAllTheaters() {
        return ResponseEntity.ok(theaterService.getAllTheaters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theater> getTheaterById(@PathVariable Long id) {
        return ResponseEntity.ok(theaterService.getTheaterById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Theater> updateTheater(@PathVariable Long id, @RequestBody Theater theater) {
        return ResponseEntity.ok(theaterService.updateTheater(id, theater));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{theaterId}/screens")
    public ResponseEntity<Screen> createScreen(@PathVariable Long theaterId, @RequestBody Screen screen) {
        return ResponseEntity.ok(theaterService.createScreen(theaterId, screen));
    }

    @GetMapping("/{theaterId}/screens")
    public ResponseEntity<List<Screen>> getScreensByTheater(@PathVariable Long theaterId) {
        return ResponseEntity.ok(theaterService.getScreensByTheater(theaterId));
    }

    @PutMapping("/screens/{screenId}")
    public ResponseEntity<Screen> updateScreen(@PathVariable Long screenId, @RequestBody Screen screen) {
        return ResponseEntity.ok(theaterService.updateScreen(screenId, screen));
    }

    @DeleteMapping("/screens/{screenId}")
    public ResponseEntity<Void> deleteScreen(@PathVariable Long screenId) {
        theaterService.deleteScreen(screenId);
        return ResponseEntity.noContent().build();
    }
}
