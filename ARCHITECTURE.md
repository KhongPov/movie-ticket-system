# Movie Ticket System - Component Architecture & API Integration Guide

## 📐 Application Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     React Frontend                          │
│                  (Port: 5173)                               │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌────────────────┐  ┌────────────────┐  ┌────────────────┐
│  │  Pages         │  │  Components    │  │  Stores        │
│  ├────────────────┤  ├────────────────┤  ├────────────────┤
│  │ HomePage       │  │ Navbar         │  │ useAuthStore   │
│  │ MovieDetail    │  │ MovieCard      │  │                │
│  │ BookingPage    │  │ SeatGrid       │  └────────────────┘
│  │ PaymentPage    │  │ ReviewForm     │
│  │ MyBookings     │  │ BookingCard    │
│  │ Login/Register │  └────────────────┘
│  └────────────────┘
│         │                    │                    │
│         └────────────────────┴────────────────────┘
│                      │
│                      ▼
│         ┌─────────────────────────────┐
│         │   API Service Layer         │
│         ├─────────────────────────────┤
│         │ authService                 │
│         │ movieService                │
│         │ showtimeService             │
│         │ bookingService              │
│         │ seatService                 │
│         │ reviewService               │
│         │ paymentService              │
│         └─────────────────────────────┘
│                      │
│                      ▼
│         ┌─────────────────────────────┐
│         │   Axios HTTP Client         │
│         │  (JWT Token Interceptor)    │
│         └─────────────────────────────┘
│
└─────────────────────────────────────────────────────────────┘
                         │ HTTP
                         ▼
┌─────────────────────────────────────────────────────────────┐
│              Spring Boot Backend                            │
│                (Port: 8080/api)                            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Controllers          Services           Repositories      │
│  ┌─────────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ AuthController  │  │ AuthService  │  │ UserRepo     │  │
│  │ MovieController │  │ MovieService │  │ MovieRepo    │  │
│  │ BookingCtrl     │  │ BookingServ  │  │ BookingRepo  │  │
│  │ PaymentCtrl     │  │ PaymentServ  │  │ PaymentRepo  │  │
│  │ ReviewCtrl      │  │ ReviewServ   │  │ ReviewRepo   │  │
│  │ SeatCtrl        │  │ SeatServ     │  │ SeatRepo     │  │
│  │ ShowtimeCtrl    │  │ ShowtimeServ │  │ ShowtimeRepo │  │
│  └─────────────────┘  └──────────────┘  └──────────────┘  │
│         │                    │                    │        │
│         └────────────────────┴────────────────────┘        │
│                                                             │
└─────────────────────────────────────────────────────────────┘
                         │ JDBC
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                   MySQL Database                           │
│          (localhost:3306/movie_ticket_db)                  │
├─────────────────────────────────────────────────────────────┤
│ Tables: users, movies, showtimes, bookings, tickets,       │
│         seats, reviews, payments, roles, theaters          │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔄 Data Flow Diagrams

### Authentication Flow

```
User Input (Login/Register)
         │
         ▼
  Frontend Form
         │
         ▼
  authService.login/register()
         │
         ▼
  POST /auth/login
         │
         ▼
  Spring AuthController
         │
         ▼
  AuthService (JWT Generation)
         │
         ▼
  Return: { token, user }
         │
         ▼
  Store in localStorage
  Set Zustand state
         │
         ▼
  Axios Interceptor: Add Authorization header
         │
         ▼
  Redirect to Home Page
```

### Movie Booking Flow

```
1. BROWSE MOVIES
   HomePage → GET /movies → Display movies

2. VIEW DETAILS
   MovieDetail → GET /movie/{id} → GET /reviews/movie/{id}

3. SELECT SHOWTIME
   Select showtime → Navigate to booking page

4. SELECT SEATS
   BookingPage → GET /seats/screen/{screenId}
   User selects seats (stored locally)

5. CREATE BOOKING
   POST /bookings → Backend creates booking
   Returns BookingDTO with booking ID

6. PROCESS PAYMENT
   PaymentPage → POST /payments/process
   Returns payment status

7. CONFIRMATION
   Navigate to My Bookings → GET /bookings/user/my-bookings
   Display booking confirmation
```

---

## 🎯 Component Communication

### State Management (Zustand)

```typescript
// Global State
useAuthStore
├── user: User | null
├── token: string | null
├── isAuthenticated: boolean
├── login(response): void
├── logout(): void
└── setUser(user): void
```

### Service Layer

```typescript
authService
├── login(credentials)
├── register(data)
├── logout()
├── getToken()
├── getUser()
└── isAuthenticated()

movieService
├── getAllMovies()
├── getMovieById(id)
├── searchMovies(query)
├── getMoviesByGenre(genre)
└── getUpcomingMovies()

bookingService
├── createBooking(data)
├── getBookingById(id)
├── getMyBookings()
├── getUpcomingBookings()
├── getCompletedBookings()
└── cancelBooking(id)

seatService
├── getScreenSeats(screenId)
├── getAvailableSeats(screenId)
└── initializeScreenSeats(screenId, totalSeats)

reviewService
├── createReview(data)
├── getReviewsByMovie(movieId)
├── getMovieReviewStats(movieId)
├── updateReview(id, data)
└── deleteReview(id)

paymentService
├── processPayment(data)
├── getPaymentStatus(id)
├── getPaymentByBooking(bookingId)
└── refundPayment(id)

showtimeService
├── getShowtimeById(id)
├── getShowtimesByMovie(movieId)
├── getUpcomingShowtimes(movieId)
├── getAvailableSeats(showtimeId)
└── createShowtime(data)
```

---

## 📊 API Request/Response Examples

### Login Endpoint

```javascript
// Request
POST /auth/login
{
  "email": "user@example.com",
  "password": "password123"
}

// Response (200 OK)
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "name": "John Doe",
    "role": "USER"
  }
}
```

### Get Movies Endpoint

```javascript
// Request
GET /movies

// Response (200 OK)
[
  {
    "id": 1,
    "title": "The Matrix",
    "description": "A hacker learns about the true nature...",
    "genre": "ACTION",
    "duration": 136,
    "posterUrl": "https://...",
    "rating": 8.7,
    "status": "ACTIVE"
  },
  ...
]
```

### Create Booking Endpoint

```javascript
// Request
POST /bookings
Authorization: Bearer <token>
{
  "showtimeId": 5,
  "seatNumbers": ["A1", "A2", "B1"]
}

// Response (200 OK)
{
  "id": 10,
  "reference": "BOOKING_12345",
  "showtimeId": 5,
  "userId": 1,
  "totalPrice": 750,
  "status": "PENDING",
  "bookingDate": "2024-01-15T10:30:00",
  "tickets": [
    {
      "id": 101,
      "seatNumber": "A1",
      "ticketPrice": 250
    },
    ...
  ]
}
```

### Process Payment Endpoint

```javascript
// Request
POST /payments/process
Authorization: Bearer <token>
{
  "bookingId": 10,
  "amount": 750,
  "paymentMethod": "CARD"
}

// Response (200 OK)
{
  "id": 20,
  "bookingId": 10,
  "amount": 750,
  "status": "SUCCESS",
  "paymentMethod": "CARD",
  "transactionId": "txn_1234567890",
  "createdAt": "2024-01-15T10:35:00"
}
```

---

## 🔐 Authentication & Authorization

### JWT Token Flow

```
1. User logs in
   └─> Backend generates JWT token (valid for 24 hours)
   └─> Token = Header.Payload.Signature

2. Frontend stores token in localStorage
   └─> Axios interceptor adds token to all requests
   └─> Authorization: Bearer <token>

3. Backend validates token in every request
   └─> JWT filter checks token validity
   └─> Returns user context if valid
   └─> Throws 401 Unauthorized if invalid

4. When token expires
   └─> Axios interceptor detects 401
   └─> Removes token from localStorage
   └─> Redirects to login page
```

### Protected Routes

```typescript
// Protected Route Component
function ProtectedRoute({ children }) {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  return isAuthenticated ? children : <Navigate to="/login" />;
}

// Usage
<Route
  path="/booking/:showtimeId"
  element={
    <ProtectedRoute>
      <BookingPage />
    </ProtectedRoute>
  }
/>;
```

---

## 🎨 Frontend Page Structure

### HomePage

```
┌─ Navbar (Sticky)
│  ├─ Logo
│  ├─ Navigation Links
│  └─ Auth Options
│
├─ Search Bar
├─ Genre Filter Buttons
└─ Movies Grid
   └─ Movie Cards (Clickable)
      ├─ Poster Image
      ├─ Title
      ├─ Genre
      ├─ Duration
      └─ Rating
```

### MovieDetailPage

```
┌─ Movie Header
│  ├─ Poster
│  ├─ Title, Description
│  └─ Meta Info (Genre, Duration, Rating)
│
├─ Showtimes Section
│  ├─ Available Showtimes (Cards)
│  └─ Book Button
│
└─ Reviews Section
   ├─ Review Form (If authenticated)
   └─ Reviews List
      ├─ User Name
      ├─ Rating
      ├─ Comment
      └─ Date
```

### BookingPage

```
┌─ Showtime Info
│
├─ Seats Container
│  ├─ Screen Text
│  └─ Seat Grid
│     └─ Seat Buttons (Status: available/occupied/selected)
│
├─ Booking Summary (Sticky)
│  ├─ Selected Seats
│  ├─ Price Breakdown
│  └─ Proceed Button
│
└─ Seat Legend
   ├─ Available (Green)
   ├─ Occupied (Gray)
   └─ Selected (Red)
```

---

## ⚡ Performance Considerations

### Frontend Optimization

1. **Component Lazy Loading**: Pages loaded on demand
2. **API Response Caching**: Store user data in state
3. **Image Optimization**: Use responsive images
4. **CSS Minimization**: Production build optimizes CSS
5. **Bundle Splitting**: Vite handles code splitting

### Backend Optimization

1. **Database Indexing**: Indexes on frequently queried columns
2. **Query Optimization**: Efficient JPA queries
3. **Caching**: Spring cache for movie data
4. **Pagination**: Limit large result sets
5. **Connection Pooling**: Efficient DB connections

---

## 🧪 Testing Scenarios

### User Journey: Complete Booking

```
1. NEW USER
   └─ Register account → Auto-login

2. BROWSE & SELECT
   └─ Home → Search "Movie Title" → View Details

3. CHOOSE SHOWTIME
   └─ Click on showtime → Redirect to booking

4. SELECT SEATS
   └─ Click available seats → Confirm selection

5. PAYMENT
   └─ Enter card details → Process payment

6. CONFIRMATION
   └─ View booking confirmation → My Bookings

7. REVIEW
   └─ Write review → Rate movie
```

---

## 📦 Deployment Checklist

- [ ] Backend: Build JAR file
- [ ] Backend: Configure production environment variables
- [ ] Frontend: Build React app (`npm run build`)
- [ ] Frontend: Deploy dist folder to web server
- [ ] Database: Backup and migrate to production
- [ ] Security: Update JWT secret
- [ ] SSL: Configure HTTPS certificates
- [ ] CORS: Set allowed origins
- [ ] Monitoring: Set up logging & error tracking
- [ ] Testing: Complete smoke testing

---

## 🔧 File Modifications Guide

To add new features:

1. **New Endpoint**: Create controller → service → repository
2. **New API Call**: Add method in service file
3. **New UI Component**: Create component in `src/components/`
4. **New Page**: Create page in `src/pages/` and add route
5. **New Styles**: Create CSS file in `src/styles/`
6. **State**: Add to `useAuthStore` if needed

---

**Happy coding! 🎬**
