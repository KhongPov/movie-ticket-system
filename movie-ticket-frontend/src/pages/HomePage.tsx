import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { movieService } from "../services/movieService";
import type { MovieDTO } from "../services/movieService";
import "../styles/home.css";

const GENRES = [
  { label: "All", value: "" },
  { label: "Action", value: "ACTION" },
  { label: "Comedy", value: "COMEDY" },
  { label: "Drama", value: "DRAMA" },
  { label: "Horror", value: "HORROR" },
  { label: "Sci-Fi", value: "SCI_FI" },
  { label: "Romance", value: "ROMANCE" },
];

export function HomePage() {
  const navigate = useNavigate();
  const [movies, setMovies] = useState<MovieDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const [selectedGenre, setSelectedGenre] = useState("");

  useEffect(() => {
    loadMovies();
  }, []);

  const loadMovies = async () => {
    try {
      setLoading(true);
      const data = await movieService.getAllMovies();
      setMovies(data);
      setError("");
    } catch (err) {
      setError("Failed to load movies");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!searchQuery.trim()) {
      await loadMovies();
      return;
    }

    try {
      setLoading(true);
      const data = await movieService.searchMovies(searchQuery);
      setMovies(data);
      setError("");
    } catch (err) {
      setError("Search failed");
    } finally {
      setLoading(false);
    }
  };

  const handleGenreFilter = async (genre: string) => {
    setSelectedGenre(genre);
    if (!genre) {
      await loadMovies();
      return;
    }

    try {
      setLoading(true);
      const data = await movieService.getMoviesByGenre(genre);
      setMovies(data);
      setError("");
    } catch (err) {
      setError("Failed to filter by genre");
    } finally {
      setLoading(false);
    }
  };

  const renderMovies = () => {
    if (loading) {
      return <div className="loading">Loading movies...</div>;
    }

    if (!movies.length) {
      return (
        <div className="empty-state">
          <h3>No movies found</h3>
          <p>Try another keyword or pick a different genre.</p>
        </div>
      );
    }

    return (
      <div className="movies-grid">
        {movies.map((movie) => {
          const descriptionText = movie.description
            ? movie.description.trim()
            : "";
          const hasDescription = descriptionText.length > 0;
          const descriptionPreview = hasDescription
            ? `${descriptionText.slice(0, 90)}${
                descriptionText.length > 90 ? "…" : ""
              }`
            : "No description available.";
          const ratingLabel =
            typeof movie.rating === "number"
              ? movie.rating.toFixed(1)
              : "N/A";

          return (
            <div
              key={movie.id}
              className="movie-card"
              onClick={() => navigate(`/movie/${movie.id}`)}
            >
              <div className="movie-poster">
                <img
                  src={movie.posterUrl || ""}
                  alt={movie.title}
                  loading="lazy"
                  onError={(e) => {
                    const target = e.target as HTMLImageElement;
                    target.src =
                      "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='320' height='480'%3E%3Crect fill='%231a1a1a' width='320' height='480'/%3E%3Ctext x='50%25' y='50%25' fill='%23666' font-size='16' text-anchor='middle' dominant-baseline='middle'%3ENo Image%3C/text%3E%3C/svg%3E";
                  }}
                />
              </div>
              <div className="movie-info">
                <div className="movie-meta">
                  <span className="badge">{movie.genre || "N/A"}</span>
                  <span className="duration">
                    {movie.duration ? `${movie.duration} min` : "Duration TBC"}
                  </span>
                </div>
                <h3>{movie.title}</h3>
                <p className="description">{descriptionPreview}</p>
                <div className="movie-footer">
                  <span className="rating">⭐ {ratingLabel}</span>
                  <button
                    type="button"
                    className="details-btn"
                    onClick={(event) => {
                      event.stopPropagation();
                      navigate(`/movie/${movie.id}`);
                    }}
                  >
                    View Details
                  </button>
                </div>
              </div>
            </div>
          );
        })}
      </div>
    );
  };

  return (
    <div className="home-page">
      <section className="home-hero">
        <div className="hero-text">
          <p className="eyebrow">Now booking</p>
          <h1>Book the perfect movie night</h1>
          <p className="subtitle">
            Discover what&apos;s playing, search by title, and lock your seats
            before they sell out.
          </p>
        </div>
        <form onSubmit={handleSearch} className="search-form">
          <label htmlFor="movie-search" className="sr-only">
            Search movies
          </label>
          <input
            id="movie-search"
            type="text"
            placeholder="Search movies, actors, or genres"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
          <button type="submit">Search</button>
        </form>
      </section>

      <section className="filter-section">
        <div className="filter-header">
          <div>
            <h2>Browse by genre</h2>
            <p>Tap a genre to instantly filter the catalog.</p>
          </div>
          <span className="movie-count">
            {movies.length} {movies.length === 1 ? "movie" : "movies"}
          </span>
        </div>
        <div className="genre-filter">
          {GENRES.map((genre) => (
            <button
              key={genre.value || "ALL"}
              type="button"
              className={genre.value === selectedGenre ? "active" : ""}
              onClick={() => handleGenreFilter(genre.value)}
            >
              {genre.label}
            </button>
          ))}
        </div>
      </section>

      {error && <div className="error-message">{error}</div>}

      {renderMovies()}
    </div>
  );
}
