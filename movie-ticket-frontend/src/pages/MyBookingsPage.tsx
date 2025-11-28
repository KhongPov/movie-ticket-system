import { useEffect, useState } from "react";
import { bookingService } from "../services/bookingService";
import type { BookingDTO } from "../services/bookingService";
import "../styles/mybookings.css";

export function MyBookingsPage() {
  const [bookings, setBookings] = useState<BookingDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [activeTab, setActiveTab] = useState<"all" | "upcoming" | "completed">(
    "all"
  );

  useEffect(() => {
    loadBookings();
  }, [activeTab]);

  const loadBookings = async () => {
    try {
      setLoading(true);
      let data;
      if (activeTab === "all") {
        data = await bookingService.getMyBookings();
      } else if (activeTab === "upcoming") {
        data = await bookingService.getUpcomingBookings();
      } else {
        data = await bookingService.getCompletedBookings();
      }
      setBookings(data);
    } catch (err) {
      setError("Failed to load bookings");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleCancelBooking = async (bookingId: number) => {
    if (!window.confirm("Are you sure you want to cancel this booking?")) {
      return;
    }

    try {
      await bookingService.cancelBooking(bookingId);
      loadBookings();
    } catch (err) {
      setError("Failed to cancel booking");
      console.error(err);
    }
  };

  return (
    <div className="my-bookings-page">
      <h1>My Bookings</h1>

      <div className="tabs">
        <button
          className={activeTab === "all" ? "active" : ""}
          onClick={() => setActiveTab("all")}
        >
          All Bookings
        </button>
        <button
          className={activeTab === "upcoming" ? "active" : ""}
          onClick={() => setActiveTab("upcoming")}
        >
          Upcoming
        </button>
        <button
          className={activeTab === "completed" ? "active" : ""}
          onClick={() => setActiveTab("completed")}
        >
          Completed
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}

      {loading ? (
        <div className="loading">Loading...</div>
      ) : bookings.length === 0 ? (
        <div className="no-bookings">No bookings found</div>
      ) : (
        <div className="bookings-list">
          {bookings.map((booking) => (
            <div key={booking.id} className="booking-card">
              <div className="booking-header">
                <h3>Booking #{booking.bookingReference}</h3>
                <span className={`status ${booking.status.toLowerCase()}`}>
                  {booking.status}
                </span>
              </div>
              <div className="booking-details">
                <p>
                  <strong>Movie:</strong> {booking.movieTitle}
                </p>
                <p>
                  <strong>Theater:</strong> {booking.theaterName}
                </p>
                <p>
                  <strong>Showtime:</strong>{" "}
                  {new Date(booking.showDateTime).toLocaleString()}
                </p>
                <p>
                  <strong>Seats:</strong>{" "}
                  {booking.tickets && booking.tickets.length > 0
                    ? booking.tickets.map((t) => t.seatNumber).join(", ")
                    : "Pending"}
                </p>
                <p>
                  <strong>Total Paid:</strong> ₹
                  {Number(booking.totalAmount ?? 0).toFixed(2)}
                </p>
              </div>
              {booking.status === "CONFIRMED" && (
                <button
                  className="cancel-btn"
                  onClick={() => handleCancelBooking(booking.id)}
                >
                  Cancel Booking
                </button>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
