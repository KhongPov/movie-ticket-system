import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { showtimeService } from "../services/showtimeService";
import type { ShowtimeDTO } from "../services/showtimeService";
import { seatService } from "../services/seatService";
import type { ScreenSeatsDTO, SeatDTO } from "../services/seatService";
import { bookingService } from "../services/bookingService";
import "../styles/booking.css";

export function BookingPage() {
  const { showtimeId } = useParams<{ showtimeId: string }>();
  const navigate = useNavigate();

  const [showtime, setShowtime] = useState<ShowtimeDTO | null>(null);
  const [screenSeats, setScreenSeats] = useState<ScreenSeatsDTO | null>(null);
  const [selectedSeats, setSelectedSeats] = useState<SeatDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [bookingLoading, setBookingLoading] = useState(false);

  useEffect(() => {
    loadBookingData();
  }, [showtimeId]);

  const loadBookingData = async () => {
    if (!showtimeId) return;

    try {
      setLoading(true);
      const showtimeData = await showtimeService.getShowtimeById(
        Number(showtimeId)
      );
      const seatsData = await seatService.getScreenSeats(showtimeData.screenId);

      setShowtime(showtimeData);
      setScreenSeats(seatsData);
    } catch (err) {
      setError("Failed to load booking details");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const toggleSeat = (seat: SeatDTO) => {
    if (seat.status !== "AVAILABLE") return;

    setSelectedSeats((prev) =>
      prev.some((s) => s.id === seat.id)
        ? prev.filter((s) => s.id !== seat.id)
        : [...prev, seat]
    );
  };

  const handleConfirmBooking = async () => {
    if (selectedSeats.length === 0) {
      setError("Please select at least one seat");
      return;
    }

    if (!showtimeId) return;

    try {
      setBookingLoading(true);
      const booking = await bookingService.createBooking({
        showtimeId: Number(showtimeId),
        seatIds: selectedSeats.map((s) => s.id),
      });

      navigate(`/payment/${booking.id}`);
    } catch (err) {
      setError("Failed to create booking");
      console.error(err);
    } finally {
      setBookingLoading(false);
    }
  };

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error-message">{error}</div>;
  if (!showtime || !screenSeats)
    return <div className="error-message">Data not found</div>;

  const totalPrice = selectedSeats.length * showtime.ticketPrice;

  return (
    <div className="booking-page">
      <div className="showtime-info">
        <h1>{showtime.movieTitle}</h1>
        <p className="showtime-details">
          {new Date(showtime.showDateTime).toLocaleString()} •{" "}
          {showtime.theaterName} • {showtime.screenNumber}
        </p>
      </div>

      <div className="seats-container">
        <div className="screen">SCREEN</div>
        <div className="seats-grid">
          {screenSeats.seats.map((seat) => (
            <button
              key={seat.id}
              className={`seat ${
                seat.status === "BOOKED"
                  ? "occupied"
                  : seat.status.toLowerCase()
              } ${
                selectedSeats.some((s) => s.id === seat.id) ? "selected" : ""
              }`}
              onClick={() => toggleSeat(seat)}
              disabled={seat.status !== "AVAILABLE"}
              title={`${seat.seatNumber} (${seat.seatType}) - ${seat.status}`}
            >
              {seat.seatNumber}
            </button>
          ))}
        </div>
      </div>

      <div className="booking-summary">
        <h2>Booking Summary</h2>
        <div className="selected-seats">
          <h3>Selected Seats:</h3>
          {selectedSeats.length === 0 ? (
            <p>No seats selected</p>
          ) : (
            <p>
              {selectedSeats
                .map((s) => `${s.seatNumber} (${s.seatType})`)
                .join(", ")}
            </p>
          )}
        </div>
        <div className="price-breakdown">
          <p>
            {selectedSeats.length} seat(s) × ₹{showtime.ticketPrice} ={" "}
            <strong>₹{totalPrice}</strong>
          </p>
        </div>
        <button
          className="confirm-btn"
          onClick={handleConfirmBooking}
          disabled={selectedSeats.length === 0 || bookingLoading}
        >
          {bookingLoading ? "Processing..." : "Proceed to Payment"}
        </button>
      </div>

      <div className="seat-legend">
        <div className="legend-item">
          <div className="seat available"></div>
          <span>Available</span>
        </div>
        <div className="legend-item">
          <div className="seat occupied"></div>
          <span>Occupied</span>
        </div>
        <div className="legend-item">
          <div className="seat selected"></div>
          <span>Selected</span>
        </div>
      </div>
    </div>
  );
}
