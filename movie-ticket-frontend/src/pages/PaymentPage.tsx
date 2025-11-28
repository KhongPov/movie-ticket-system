import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { paymentService } from "../services/paymentService";
import { bookingService } from "../services/bookingService";
import type { BookingDTO } from "../services/bookingService";
import "../styles/payment.css";

export function PaymentPage() {
  const { bookingId } = useParams<{ bookingId: string }>();
  const navigate = useNavigate();

  const [booking, setBooking] = useState<BookingDTO | null>(null);
  const [cardNumber, setCardNumber] = useState("");
  const [cardHolder, setCardHolder] = useState("");
  const [expiryDate, setExpiryDate] = useState("");
  const [cvv, setCvv] = useState("");
  const [pageLoading, setPageLoading] = useState(true);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    const loadBooking = async () => {
      if (!bookingId) return;

      try {
        setPageLoading(true);
        const bookingData = await bookingService.getBookingById(
          Number(bookingId)
        );
        setBooking(bookingData);
        setError("");
      } catch (err) {
        setError("Failed to load booking details");
        console.error(err);
      } finally {
        setPageLoading(false);
      }
    };

    loadBooking();
  }, [bookingId]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!bookingId) return;
    if (!booking) {
      setError("Booking not found");
      return;
    }

    try {
      setLoading(true);
      setError("");

      // TODO: In production, use Stripe token instead of sending card details
      const response = await paymentService.processPayment({
        bookingId: Number(bookingId),
        paymentMethod: "CARD",
      });

      if (response.status === "SUCCESS" || response.status === "COMPLETED") {
        setSuccess(true);
        setTimeout(() => {
          navigate(`/my-bookings`);
        }, 2000);
      } else {
        setError("Payment failed. Please try again.");
      }
    } catch (err: any) {
      setError(err.response?.data?.message || "Payment processing failed");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  if (pageLoading) {
    return <div className="payment-page loading">Loading booking...</div>;
  }

  if (!booking) {
    return (
      <div className="payment-page">
        <div className="error-message">
          Unable to find booking. Please go back and try again.
        </div>
      </div>
    );
  }

  if (success) {
    return (
      <div className="payment-page">
        <div className="success-message">
          <h1>✓ Payment Successful</h1>
          <p>Your booking has been confirmed!</p>
          <p>Redirecting to your bookings...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="payment-page">
      <div className="payment-card">
        <div className="payment-card-header">
          <h1>Complete Your Payment</h1>
          <p>Secure checkout powered by MovieTick</p>
        </div>
        {error && <div className="error-message">{error}</div>}

        <div className="booking-summary">
          <h2>Booking Summary</h2>
          <p>
            <strong>Reference:</strong> {booking.bookingReference}
          </p>
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
          <p className="total-amount">
            Total: ₹{Number(booking.totalAmount ?? 0).toFixed(2)}
          </p>
        </div>

        <form onSubmit={handleSubmit} className="payment-form">
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="cardHolder">Card Holder Name</label>
              <input
                id="cardHolder"
                type="text"
                value={cardHolder}
                onChange={(e) => setCardHolder(e.target.value)}
                placeholder="John Doe"
                required
                disabled={loading}
              />
            </div>
            <div className="form-group">
              <label htmlFor="cardNumber">
                Card Number <span className="card-icons">VISA · MC · AMEX</span>
              </label>
              <input
                id="cardNumber"
                type="text"
                value={cardNumber}
                onChange={(e) =>
                  setCardNumber(
                    e.target.value
                      .replace(/\D/g, "")
                      .slice(0, 16)
                      .replace(/(\d{4})(?=\d)/g, "$1 ")
                  )
                }
                placeholder="1234 5678 9012 3456"
                required
                disabled={loading}
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="expiryDate">Expiry Date</label>
              <input
                id="expiryDate"
                type="text"
                value={expiryDate}
                onChange={(e) => setExpiryDate(e.target.value)}
                placeholder="MM/YY"
                maxLength={5}
                required
                disabled={loading}
              />
            </div>

            <div className="form-group">
              <label htmlFor="cvv">CVV</label>
              <input
                id="cvv"
                type="text"
                value={cvv}
                onChange={(e) => setCvv(e.target.value.replace(/\D/g, "").slice(0,4))}
                placeholder="123"
                required
                disabled={loading}
              />
            </div>
          </div>

          <div className="form-actions">
            <button type="submit" disabled={loading}>
              {loading ? "Processing..." : `Pay ₹${Number(booking.totalAmount ?? 0).toFixed(2)}`}
            </button>
            <button
              type="button"
              className="secondary-btn"
              onClick={() => navigate("/my-bookings")}
              disabled={loading}
            >
              View My Bookings
            </button>
          </div>
        </form>

        <div className="payment-note">
          <p>
            💡 For testing, use any card details. This is a demo payment form.
          </p>
          <p>🔒 Your information is not stored or sent anywhere outside this demo.</p>
        </div>
      </div>
    </div>
  );
}
