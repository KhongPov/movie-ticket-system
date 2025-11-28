import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { movieService } from "../services/movieService";
import type { MovieDTO } from "../services/movieService";
import { showtimeService } from "../services/showtimeService";
import type { ShowtimeDTO } from "../services/showtimeService";
import { reviewService } from "../services/reviewService";
import type { ReviewDTO, MovieReviewStatsDTO } from "../services/reviewService";
import { useAuthStore } from "../store/useAuthStore";
import "../styles/movieDetail.css";

export function MovieDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);

  const [movie, setMovie] = useState<MovieDTO | null>(null);
  const [showtimes, setShowtimes] = useState<ShowtimeDTO[]>([]);
  const [reviews, setReviews] = useState<ReviewDTO[]>([]);
  const [stats, setStats] = useState<MovieReviewStatsDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [selectedShowtime, setSelectedShowtime] = useState<ShowtimeDTO | null>(
    null
  );
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState("");
  const [reviewLoading, setReviewLoading] = useState(false);

  useEffect(() => {
    loadMovieDetails();
  }, [id]);

  const loadMovieDetails = async () => {
    if (!id) return;

    try {
      setLoading(true);
      const [movieData, showtimesData, reviewsData, statsData] =
        await Promise.all([
          movieService.getMovieById(Number(id)),
          showtimeService.getUpcomingShowtimes(Number(id)),
          reviewService.getReviewsByMovie(Number(id)),
          reviewService.getMovieReviewStats(Number(id)),
        ]);

      setMovie(movieData);
      setShowtimes(showtimesData);
      setReviews(reviewsData);
      setStats(statsData);
    } catch (err) {
      setError("Failed to load movie details");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleBooking = () => {
    if (!isAuthenticated) {
      navigate("/login");
      return;
    }
    if (selectedShowtime) {
      navigate(`/booking/${selectedShowtime.id}`);
    }
  };

  const handleReviewSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!id) return;

    try {
      setReviewLoading(true);
      await reviewService.createReview({
        movieId: Number(id),
        rating,
        comment,
      });
      setComment("");
      setRating(5);
      loadMovieDetails();
    } catch (err) {
      setError("Failed to submit review");
      console.error(err);
    } finally {
      setReviewLoading(false);
    }
  };

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error-message">{error}</div>;
  if (!movie) return <div className="error-message">Movie not found</div>;

  return (
    <div className="movie-detail-page">
      <div className="movie-header">
        <img src={movie.posterUrl || "/placeholder.jpg"} alt={movie.title} />
        <div className="movie-details">
          <h1>{movie.title}</h1>
          <p className="description">{movie.description}</p>
          <div className="meta-info">
            <span className="genre">{movie.genre}</span>
            <span className="duration">{movie.duration} minutes</span>
            <span className="rating">
              ⭐ {stats?.averageRating || movie.rating || "N/A"}
            </span>
            <span className="reviews">
              ({stats?.totalReviews || 0} reviews)
            </span>
          </div>
        </div>
      </div>

      <div className="showtimes-section">
        <h2>Available Showtimes</h2>
        {showtimes.length === 0 ? (
          <p>No upcoming showtimes available</p>
        ) : (
          <div className="showtimes-grid">
            {showtimes.map((showtime) => (
              <div
                key={showtime.id}
                className={`showtime-card ${
                  selectedShowtime?.id === showtime.id ? "selected" : ""
                }`}
                onClick={() => setSelectedShowtime(showtime)}
              >
                <p className="time">
                  {new Date(showtime.showDateTime).toLocaleString()}
                </p>
                <p className="price">₹{showtime.ticketPrice}</p>
                <p className="seats">
                  {showtime.theaterName} • {showtime.screenNumber}
                </p>
                <p className="seats">Seats left: {showtime.availableSeats}</p>
              </div>
            ))}
          </div>
        )}
        {selectedShowtime && (
          <button className="book-btn" onClick={handleBooking}>
            Book Tickets
          </button>
        )}
      </div>

      <div className="reviews-section">
        <h2>Reviews</h2>

        {isAuthenticated && (
          <form onSubmit={handleReviewSubmit} className="review-form">
            <div className="form-group">
              <label htmlFor="rating">Rating (1-5)</label>
              <input
                id="rating"
                type="number"
                min="1"
                max="5"
                value={rating}
                onChange={(e) => setRating(Number(e.target.value))}
              />
            </div>
            <div className="form-group">
              <label htmlFor="comment">Review</label>
              <textarea
                id="comment"
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                placeholder="Share your thoughts..."
              />
            </div>
            <button type="submit" disabled={reviewLoading}>
              {reviewLoading ? "Submitting..." : "Submit Review"}
            </button>
          </form>
        )}

        <div className="reviews-list">
          {reviews.map((review) => (
            <div key={review.id} className="review-card">
              <div className="review-header">
                <strong>{review.userName}</strong>
                <span className="rating">⭐ {review.rating}/5</span>
              </div>
              <p>{review.comment}</p>
              <small>{new Date(review.createdAt).toLocaleDateString()}</small>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
