package com.movieticket.service;

import com.movieticket.dto.CreateReviewRequest;
import com.movieticket.dto.MovieReviewStatsDTO;
import com.movieticket.dto.ReviewDTO;
import com.movieticket.entity.Movie;
import com.movieticket.entity.Review;
import com.movieticket.entity.User;
import com.movieticket.repository.MovieRepository;
import com.movieticket.repository.ReviewRepository;
import com.movieticket.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ReviewDTO createReview(Long userId, CreateReviewRequest request) {
        if (request.getRating() < 1 || request.getRating() > 10) {
            throw new RuntimeException("Rating must be between 1 and 10");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Review review = Review.builder()
                .movie(movie)
                .user(user)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);

        // Update movie rating
        updateMovieRating(movie.getId());

        return mapToDTO(savedReview);
    }

    public ReviewDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        return mapToDTO(review);
    }

    public List<ReviewDTO> getReviewsByMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        return reviewRepository.findRecentReviewsByMovie(movieId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return reviewRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MovieReviewStatsDTO getMovieReviewStats(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        List<Review> reviews = reviewRepository.findByMovieId(movieId);

        Double averageRating = reviewRepository.getAverageRatingByMovie(movieId);
        if (averageRating == null) {
            averageRating = 0.0;
        }

        Long fiveStarCount = reviews.stream().filter(r -> r.getRating() >= 9).count();
        Long fourStarCount = reviews.stream().filter(r -> r.getRating() >= 7 && r.getRating() < 9).count();
        Long threeStarCount = reviews.stream().filter(r -> r.getRating() >= 5 && r.getRating() < 7).count();
        Long twoStarCount = reviews.stream().filter(r -> r.getRating() >= 3 && r.getRating() < 5).count();
        Long oneStarCount = reviews.stream().filter(r -> r.getRating() < 3).count();

        return MovieReviewStatsDTO.builder()
                .movieId(movie.getId())
                .movieTitle(movie.getTitle())
                .averageRating(Math.round(averageRating * 10) / 10.0)
                .totalReviews((long) reviews.size())
                .fiveStarCount(fiveStarCount)
                .fourStarCount(fourStarCount)
                .threeStarCount(threeStarCount)
                .twoStarCount(twoStarCount)
                .oneStarCount(oneStarCount)
                .build();
    }

    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        Long movieId = review.getMovie().getId();
        reviewRepository.deleteById(id);

        // Update movie rating after deletion
        updateMovieRating(movieId);
    }

    public ReviewDTO updateReview(Long id, CreateReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (request.getRating() < 1 || request.getRating() > 10) {
            throw new RuntimeException("Rating must be between 1 and 10");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review updatedReview = reviewRepository.save(review);

        // Update movie rating after update
        updateMovieRating(review.getMovie().getId());

        return mapToDTO(updatedReview);
    }

    private void updateMovieRating(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Double averageRating = reviewRepository.getAverageRatingByMovie(movieId);
        if (averageRating == null) {
            movie.setRating(0.0);
        } else {
            movie.setRating(Math.round(averageRating * 10) / 10.0);
        }

        movieRepository.save(movie);
    }

    private ReviewDTO mapToDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .movieId(review.getMovie().getId())
                .movieTitle(review.getMovie().getTitle())
                .userId(review.getUser().getId())
                .userName(review.getUser().getFirstName() + " " + review.getUser().getLastName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
