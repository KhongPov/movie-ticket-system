package com.movieticket.service;

import com.movieticket.dto.ScreenSeatsDTO;
import com.movieticket.dto.SeatDTO;
import com.movieticket.entity.Screen;
import com.movieticket.entity.Seat;
import com.movieticket.repository.ScreenRepository;
import com.movieticket.repository.SeatRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ScreenSeatsDTO getScreenSeats(Long screenId) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        List<Seat> seats = seatRepository.findByScreenId(screenId);
        List<SeatDTO> seatDTOs = seats.stream()
                .map(seat -> modelMapper.map(seat, SeatDTO.class))
                .collect(Collectors.toList());

        return ScreenSeatsDTO.builder()
                .screenId(screen.getId())
                .screenNumber(screen.getScreenNumber())
                .totalSeats(screen.getTotalSeats())
                .seats(seatDTOs)
                .build();
    }

    public void initializeScreenSeats(Long screenId, Integer totalSeats) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        // Generate seats in format A1, A2, B1, B2, etc.
        int seatsPerRow = 10;
        int rows = (int) Math.ceil((double) totalSeats / seatsPerRow);

        for (int row = 0; row < rows; row++) {
            char rowLetter = (char) ('A' + row);
            for (int col = 1; col <= seatsPerRow && (row * seatsPerRow + col) <= totalSeats; col++) {
                String seatNumber = rowLetter + String.valueOf(col);
                String seatType = "NORMAL";

                // Mark first and last rows as premium
                if (row == 0 || row == rows - 1) {
                    seatType = "PREMIUM";
                }

                Seat seat = Seat.builder()
                        .screen(screen)
                        .seatNumber(seatNumber)
                        .seatType(seatType)
                        .status("AVAILABLE")
                        .build();

                seatRepository.save(seat);
            }
        }
    }

    public void updateSeatStatus(Long seatId, String status) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
        seat.setStatus(status);
        seatRepository.save(seat);
    }

    public List<SeatDTO> getAvailableSeats(Long screenId) {
        return seatRepository.findByScreenId(screenId).stream()
                .filter(seat -> seat.getStatus().equals("AVAILABLE"))
                .map(seat -> modelMapper.map(seat, SeatDTO.class))
                .collect(Collectors.toList());
    }
}
