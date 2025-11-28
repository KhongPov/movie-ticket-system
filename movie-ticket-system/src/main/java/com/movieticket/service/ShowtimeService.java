package com.movieticket.service;

import com.movieticket.dto.CreateShowtimeRequest;
import com.movieticket.dto.ShowtimeDTO;
import com.movieticket.entity.*;
import com.movieticket.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ShowtimeDTO createShowtime(CreateShowtimeRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Theater not found"));
        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        Showtime showtime = Showtime.builder()
                .movie(movie)
                .theater(theater)
                .screen(screen)
                .showDateTime(request.getShowDateTime())
                .ticketPrice(request.getTicketPrice())
                .status("AVAILABLE")
                .build();

        Showtime savedShowtime = showtimeRepository.save(showtime);
        return mapToDTO(savedShowtime);
    }

    public ShowtimeDTO getShowtimeById(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));
        return mapToDTO(showtime);
    }

    public List<ShowtimeDTO> getShowtimesByMovie(Long movieId) {
        return showtimeRepository.findByMovieId(movieId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ShowtimeDTO> getUpcomingShowtimes(Long movieId) {
        return showtimeRepository.findUpcomingShowtimes(movieId, LocalDateTime.now())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ShowtimeDTO> getShowtimesByTheater(Long theaterId) {
        return showtimeRepository.findByTheaterId(theaterId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ShowtimeDTO> searchShowtimes(Long theaterId, LocalDateTime startDate, LocalDateTime endDate) {
        return showtimeRepository.findShowtimesByTheaterAndDateRange(theaterId, startDate, endDate)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public int getAvailableSeats(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        long bookedSeats = seatRepository.countByScreenIdAndStatus(showtime.getScreen().getId(), "BOOKED");
        return showtime.getScreen().getTotalSeats() - (int) bookedSeats;
    }

    public void updateShowtimeStatus(Long id, String status) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));
        showtime.setStatus(status);
        showtimeRepository.save(showtime);
    }

    public void cancelShowtime(Long id) {
        updateShowtimeStatus(id, "CANCELLED");
    }

    private ShowtimeDTO mapToDTO(Showtime showtime) {
        int availableSeats = getAvailableSeats(showtime.getId());
        return ShowtimeDTO.builder()
                .id(showtime.getId())
                .movieId(showtime.getMovie().getId())
                .theaterId(showtime.getTheater().getId())
                .screenId(showtime.getScreen().getId())
                .showDateTime(showtime.getShowDateTime())
                .ticketPrice(showtime.getTicketPrice())
                .status(showtime.getStatus())
                .movieTitle(showtime.getMovie().getTitle())
                .theaterName(showtime.getTheater().getName())
                .screenNumber(showtime.getScreen().getScreenNumber())
                .availableSeats(availableSeats)
                .build();
    }
}
