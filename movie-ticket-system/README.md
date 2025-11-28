  # Movie Ticket System Management

A comprehensive Java Spring Boot application for managing movie bookings, payments, and theater operations.

## Features

### User Features
- **User Registration & Login**: Email-based authentication with JWT tokens
- **Browse Movies**: View available movies with details, ratings, and showtimes
- **Search & Filter**: Search movies by title, genre, language, or rating
- **Book Tickets**: Select seats and book tickets for showtimes
- **Manage Bookings**: View booking history, upcoming bookings, and completed bookings
- **Seat Selection**: Interactive seat selection with real-time availability
- **Payment Processing**: Secure payment processing with Stripe integration
- **Rating & Review**: Rate movies (1-10) and leave comments after watching
- **Review History**: View and manage personal reviews

### Admin Features
- **Movie Management**: Add, edit, delete movies and manage status
- **Showtime Management**: Create showtimes, manage pricing, and cancel shows
- **Theater Management**: Manage theaters, screens, and seat layouts
- **Booking Management**: View all bookings, manage cancellations
- **User Management**: View user details, suspend/activate accounts
- **Dashboard**: Real-time metrics and analytics
- **Reports**: Daily and monthly reports with revenue tracking
- **Payment Management**: View payment history and process refunds

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.0
- **Database**: MySQL/PostgreSQL
- **Security**: Spring Security, JWT (JJWT)
- **Payment**: Stripe API
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Maven

## Project Structure

\`\`\`
src/main/java/com/movieticket/
├── config/           # Configuration classes (Security, ModelMapper, Stripe)
├── controller/       # REST Controllers
├── entity/          # JPA Entity classes
├── dto/             # Data Transfer Objects
├── repository/      # Spring Data JPA Repositories
├── service/         # Business Logic Services
└── security/        # JWT and Security utilities

src/main/resources/
├── application.yml  # Application configuration
\`\`\`

## API Endpoints

### Authentication
- `POST localhost:8080/auth/register` - User registration
- `POST /auth/login` - User login

### Movies (Public)
- `GET /movies` - Get all active movies
- `GET /movies/{id}` - Get movie details
- `GET /movies/search?query=...` - Search movies
- `GET /movies/genre/{genre}` - Filter by genre
- `GET /movies/upcoming` - Get upcoming movies

### Movies (Admin)
- `POST /movies` - Create movie
- `PUT /movies/{id}` - Update movie
- `DELETE /movies/{id}` - Delete movie

### Showtimes (Public)
- `GET /showtimes/{id}` - Get showtime details
- `GET /showtimes/movie/{movieId}` - Get showtimes by movie
- `GET /showtimes/movie/{movieId}/upcoming` - Get upcoming showtimes
- `GET /showtimes/theater/{theaterId}` - Get showtimes by theater
- `GET /showtimes/{showtimeId}/available-seats` - Get available seats

### Showtimes (Admin)
- `POST /showtimes` - Create showtime
- `PUT /showtimes/{id}/status/{status}` - Update showtime status
- `DELETE /showtimes/{id}` - Cancel showtime

### Seats
- `GET /seats/screen/{screenId}` - Get all seats in a screen
- `GET /seats/screen/{screenId}/available` - Get available seats
- `POST /seats/screen/{screenId}/initialize` - Initialize screen seats (Admin)

### Bookings (Authenticated)

| Action | Endpoint | Notes / Sample |
| --- | --- | --- |
| Create booking | `POST /bookings` | Body: `{"showtimeId":1,"seatIds":[10,11]}` |
| Get booking by id | `GET /bookings/{id}` | Returns booking + ticket DTOs |
| Lookup by reference | `GET /bookings/reference/{reference}` | Use the `bookingReference` returned in the create response |
| My bookings | `GET /bookings/user/my-bookings` | Requires JWT/cookie |
| Upcoming bookings | `GET /bookings/user/upcoming` | Filters by `showDateTime > now()` |
| Completed bookings | `GET /bookings/user/completed` | Shows past shows |
| Cancel booking | `DELETE /bookings/{id}` | Releases seats and flips status to `CANCELLED` |

**UI path:** Movie detail → choose showtime → `/booking/:showtimeId` → select seats → `Proceed to Payment`. The same REST endpoints drive both the React app and API clients.

### Payments (Authenticated)

| Action | Endpoint | Notes |
| --- | --- | --- |
| Process payment | `POST /payments/process` | Body: `{"bookingId":16,"paymentMethod":"CARD"}`. Backend records an offline transaction and marks booking `COMPLETED`. |
| Get payment by booking | `GET /payments/booking/{bookingId}` | Returns amount/status/transactionId |
| Refund payment (admin) | `POST /payments/{id}/refund` | Flips payment to `REFUNDED` and booking to `CANCELLED` |

**UI path:** `/payment/:bookingId` shows the booking summary, collects mock card details, and calls `POST /payments/process`. Any card data is accepted in demo mode.

### Reviews

**Public**
- `GET /reviews/{id}` – fetch single review
- `GET /reviews/movie/{movieId}` – list reviews for a movie
- `GET /reviews/movie/{movieId}/stats` – aggregated rating/count

**Authenticated**
- `POST /reviews` – body: `{"movieId":1,"rating":5,"comment":"..."}`
- `PUT /reviews/{id}`
- `DELETE /reviews/{id}`
- `GET /reviews/user/{userId}`

### Admin Dashboard

| Purpose | Endpoint | Example |
| --- | --- | --- |
| Overview metrics | `GET /admin/dashboard/metrics` | cards for revenue, bookings, etc. |
| Daily report | `GET /admin/dashboard/report/daily?date=2024-01-01` | aggregated totals for that day |
| Monthly report | `GET /admin/dashboard/report/monthly?year=2024&month=1` | monthly rollup |

### Admin Users

- `GET /admin/users` – list
- `GET /admin/users/{id}` – detail
- `PUT /admin/users/{id}/suspend` – set status
- `PUT /admin/users/{id}/activate`
- `DELETE /admin/users/{id}`

### Admin Theaters & Screens

- `POST /admin/theaters` – create theater
- `GET /admin/theaters` – list
- `GET /admin/theaters/{id}` – detail
- `PUT /admin/theaters/{id}` – update
- `DELETE /admin/theaters/{id}` – remove
- `POST /admin/theaters/{theaterId}/screens` – add screen
- `GET /admin/theaters/{theaterId}/screens` – list screens
- `PUT /admin/theaters/screens/{screenId}` – update screen
- `DELETE /admin/theaters/screens/{screenId}` – delete screen

## Setup & Installation

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8.0+ or PostgreSQL 12+
- Stripe Account

### Environment Variables

\`\`\`bash
# Database
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/movie_ticket_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root

# JWT
JWT_SECRET=your-super-secret-key-change-this-in-production

# Stripe
STRIPE_API_KEY=sk_test_your_stripe_key

# Email (Optional)
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
\`\`\`

### Build & Run

\`\`\`bash
# Clone the repository
git clone <repository-url>
cd movie-ticket-system

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
\`\`\`

The application will start on `http://localhost:8080`

## Database Schema

The system uses 11 main entities:
- **User** - User accounts with roles
- **Role** - User roles (ADMIN, USER)
- **Movie** - Movie information
- **Theater** - Theater locations
- **Screen** - Screens in theaters
- **Seat** - Individual seats in screens
- **Showtime** - Movie showtimes
- **Booking** - User bookings
- **BookingTicket** - Individual tickets in bookings
- **Payment** - Payment records
- **Review** - Movie reviews and ratings

## Authentication & Authorization

The system uses JWT-based authentication with role-based access control:

- **Public Endpoints**: Movie browsing, showtime viewing, review reading
- **User Endpoints**: Booking, payments, review management
- **Admin Endpoints**: Movie/showtime/theater management, user management, reports

JWT tokens are included in the `Authorization: Bearer <token>` header.

## Payment Integration

The system integrates with Stripe for secure payment processing:

1. User creates booking
2. System calculates total amount
3. Frontend sends Stripe token
4. Backend processes payment with Stripe
5. Payment status is stored in database
6. Booking status is updated to COMPLETED

Refunds can be processed by admins through the payment endpoint.

## Error Handling

The system provides consistent error responses:

\`\`\`json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Error description",
  "path": "/api/endpoint"
}
\`\`\`

## Performance Considerations

- **Database Indexing**: Add indexes on frequently queried columns (user_id, movie_id, showtime_id)
- **Caching**: Consider implementing Redis for caching popular movies and showtimes
- **Connection Pool**: Configure HikariCP for optimal database connection management
- **Transaction Management**: Uses @Transactional for data consistency

## Security Best Practices

- Passwords are hashed with BCrypt
- JWT tokens have expiration times
- Role-based access control on all admin endpoints
- SQL injection prevention through parameterized queries
- CORS configured for frontend integration
- Stripe tokens handled securely on the backend

## Testing

Run tests with:
\`\`\`bash
mvn test
\`\`\`

## Future Enhancements

- Email notifications for booking confirmations
- SMS notifications for reminders
- Advanced caching with Redis
- Microservices architecture
- Mobile app integration
- Multi-language support
- Seat allocation optimization
- Subscription/membership plans
- Social media integration

## License

This project is licensed under the MIT License.

## Support

For issues and questions, please create an issue in the repository.
