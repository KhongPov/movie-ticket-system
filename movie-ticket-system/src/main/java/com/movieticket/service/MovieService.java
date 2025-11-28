package com.movieticket.service;

import com.movieticket.dto.CreateMovieRequest;
import com.movieticket.dto.MovieDTO;
import com.movieticket.entity.Movie;
import com.movieticket.repository.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ModelMapper modelMapper;

    public MovieDTO createMovie(CreateMovieRequest request) {
        Movie movie = Movie.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .genre(request.getGenre())
                .language(request.getLanguage())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .posterUrl(request.getPosterUrl())
                .status("ACTIVE")
                .rating(0.0)
                .build();

        Movie savedMovie = movieRepository.save(movie);
        return modelMapper.map(savedMovie, MovieDTO.class);
    }

    public MovieDTO getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return modelMapper.map(movie, MovieDTO.class);
    }

    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    public List<MovieDTO> getActiveMovies() {
        return movieRepository.findByStatus("ACTIVE").stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    public List<MovieDTO> getMoviesByGenre(String genre) {
        return movieRepository.findByGenre(genre).stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    public List<MovieDTO> searchMovies(String searchTerm) {
        return movieRepository.searchMovies(searchTerm).stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    public List<MovieDTO> getUpcomingMovies() {
        return movieRepository.findByReleaseDateAfterAndStatus(LocalDate.now(), "ACTIVE")
                .stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    public MovieDTO updateMovie(Long id, CreateMovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setGenre(request.getGenre());
        movie.setLanguage(request.getLanguage());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setDuration(request.getDuration());
        movie.setPosterUrl(request.getPosterUrl());

        Movie updatedMovie = movieRepository.save(movie);
        return modelMapper.map(updatedMovie, MovieDTO.class);
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        movie.setStatus("ENDED");
        movieRepository.save(movie);
    }
}
