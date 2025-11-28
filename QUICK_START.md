# Quick Start Guide - Movie Ticket System

## 🚀 Start Everything in 5 Minutes

### Step 1: Backend Setup (Terminal 1)

```bash
cd movie-ticket-system

# Ensure MySQL is running and database exists
# Create database: CREATE DATABASE movie_ticket_db;

# Run the Spring Boot application
mvn spring-boot:run
```

✅ Backend runs on `http://localhost:8080/api`

### Step 2: Frontend Setup (Terminal 2)

```bash
cd movie-ticket-frontend

# Install dependencies (first time only)
npm install

# Start development server
npm run dev
```

✅ Frontend runs on `http://localhost:5173`

### Step 3: Access the Application

Open browser and go to: **`http://localhost:5173`**

---

## 🎯 Application Flow

### 1. **Homepage** (`/`)

- View all available movies
- Search by title
- Filter by genre

### 2. **Movie Details** (`/movie/:id`)

- Full movie information
- Available showtimes
- Customer reviews
- Add your own review (if logged in)

### 3. **Register/Login**

- Click "Register" for new account
- Or "Login" with existing credentials
- Auto-redirects after successful auth

### 4. **Book Tickets**

- Click on showtime
- Select seats (interactive grid)
- See real-time price calculation
- Click "Proceed to Payment"

### 5. **Payment** (`/payment/:bookingId`)

- Enter card details (demo mode - any valid format)
- Click "Pay Now"
- Redirect to "My Bookings" on success

### 6. **View Bookings** (`/my-bookings`)

- See all your bookings
- Filter by status
- Cancel pending bookings

---

## 📁 File Structure Overview

### Backend Services

| Service           | Purpose                       |
| ----------------- | ----------------------------- |
| `AuthService`     | User registration & login     |
| `MovieService`    | Movie management              |
| `ShowtimeService` | Showtime scheduling           |
| `BookingService`  | Booking creation & management |
| `SeatService`     | Seat management               |
| `PaymentService`  | Payment processing            |
| `ReviewService`   | Movie reviews                 |

### Frontend Pages

| Page         | Route                  | Description          |
| ------------ | ---------------------- | -------------------- |
| Home         | `/`                    | Browse movies        |
| Movie Detail | `/movie/:id`           | Movie info & reviews |
| Login        | `/login`               | User login           |
| Register     | `/register`            | New user signup      |
| Booking      | `/booking/:showtimeId` | Select seats         |
| Payment      | `/payment/:bookingId`  | Process payment      |
| My Bookings  | `/my-bookings`         | View user bookings   |

---

## 🔑 API Endpoints Quick Reference

### Auth

- `POST /auth/login` → Login user
- `POST /auth/register` → Register new user

### Movies

- `GET /movies` → All movies
- `GET /movies/:id` → Movie details
- `GET /movies/search?query=title` → Search

### Bookings

- `POST /bookings` → Create booking
- `GET /bookings/user/my-bookings` → User's bookings
- `DELETE /bookings/:id` → Cancel booking

### More at `/SETUP_GUIDE.md`

---

## 🛠️ Useful Commands

### Frontend

```bash
cd movie-ticket-frontend

npm run dev        # Start development
npm run build      # Production build
npm run lint       # Check code
npm run preview    # Preview build
```

### Backend

```bash
cd movie-ticket-system

mvn spring-boot:run          # Development
mvn clean package            # Build jar
mvn test                     # Run tests
```

---

## 🐛 Common Issues & Solutions

### "Cannot connect to backend"

```bash
# Check if backend is running
http://localhost:8080/api

# If not, make sure:
# 1. MySQL is running
# 2. Database exists: CREATE DATABASE movie_ticket_db;
# 3. Run: mvn spring-boot:run
```

### "Port 5173 already in use"

```bash
# Kill the process or use different port
npm run dev -- --port 3000
```

### "MySQL connection error"

```bash
# Check credentials in application.yml
# Default: root / no password
# Create database:
# CREATE DATABASE movie_ticket_db;
```

### "Token expired" or "Not authorized"

- Click Logout in navbar
- Click Login again
- Re-enter credentials

---

## 📊 Database Schema

### Key Tables

**Users**

- id, email, password, name, role, created_at

**Movies**

- id, title, description, genre, duration, poster_url, rating

**Showtimes**

- id, movie_id, theater_id, screen_id, start_time, ticket_price

**Bookings**

- id, user_id, showtime_id, total_price, status, booking_date

**BookingTickets**

- id, booking_id, seat_number, ticket_price

**Seats**

- id, screen_id, seat_number, status (AVAILABLE/OCCUPIED)

**Reviews**

- id, movie_id, user_id, rating, comment, created_at

---

## 🎨 Frontend Features

✅ **User Authentication** - Secure JWT-based login
✅ **Movie Browsing** - Search & filter by genre
✅ **Interactive Seats** - Visual seat selection
✅ **Real-time Pricing** - Dynamic price calculation
✅ **Review System** - Rate & review movies
✅ **Booking History** - Manage your bookings
✅ **Responsive Design** - Works on mobile & desktop
✅ **Dark Theme** - Netflix-inspired styling

---

## 🔐 Test Credentials

### Create your own:

1. Click "Register" on the app
2. Fill in details
3. Auto-login after registration

---

## 📱 Testing Checklist

- [ ] Can register new account
- [ ] Can login with credentials
- [ ] Can search & filter movies
- [ ] Can view movie details
- [ ] Can see showtimes
- [ ] Can select seats
- [ ] Can complete payment (demo)
- [ ] Can view bookings
- [ ] Can write reviews
- [ ] Mobile responsive

---

## 🚀 Next: Production Deployment

See `SETUP_GUIDE.md` for:

- Production build instructions
- Security configuration
- Database migration
- Performance optimization

---

## 📞 Need Help?

1. **Check the logs** - Terminal output shows errors
2. **Read SETUP_GUIDE.md** - Comprehensive setup
3. **Check FRONTEND_README.md** - Frontend details
4. **Backend README.md** - Backend details

---

**Enjoy building! 🎬🎭**
