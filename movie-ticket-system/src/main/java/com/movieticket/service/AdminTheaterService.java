package com.movieticket.service;

import com.movieticket.entity.Screen;
import com.movieticket.entity.Theater;
import com.movieticket.repository.ScreenRepository;
import com.movieticket.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminTheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private ScreenRepository screenRepository;

    public Theater createTheater(Theater theater) {
        return theaterRepository.save(theater);
    }

    public Theater updateTheater(Long id, Theater theaterDetails) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theater not found"));
        theater.setName(theaterDetails.getName());
        theater.setCity(theaterDetails.getCity());
        theater.setAddress(theaterDetails.getAddress());
        theater.setPhoneNumber(theaterDetails.getPhoneNumber());
        return theaterRepository.save(theater);
    }

    public void deleteTheater(Long id) {
        theaterRepository.deleteById(id);
    }

    public Screen createScreen(Long theaterId, Screen screen) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new RuntimeException("Theater not found"));
        screen.setTheater(theater);
        return screenRepository.save(screen);
    }

    public Screen updateScreen(Long screenId, Screen screenDetails) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new RuntimeException("Screen not found"));
        screen.setScreenNumber(screenDetails.getScreenNumber());
        screen.setTotalSeats(screenDetails.getTotalSeats());
        return screenRepository.save(screen);
    }

    public void deleteScreen(Long screenId) {
        screenRepository.deleteById(screenId);
    }

    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    public Theater getTheaterById(Long id) {
        return theaterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theater not found"));
    }

    public List<Screen> getScreensByTheater(Long theaterId) {
        return screenRepository.findByTheaterId(theaterId);
    }
}
