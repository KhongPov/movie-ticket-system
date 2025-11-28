# Movie Ticket System - Complete Setup Guide

## Backend Setup (Spring Boot)

### 1. Database Configuration

Ensure MySQL is running and create the database:

```sql
CREATE DATABASE movie_ticket_db;
```

### 2. Environment Variables

Create or update `.env` file in the backend directory:

```env
JWT_SECRET=your-super-secret-key-change-this-in-production
MAIL_USERNAME=your-gmail@gmail.com
MAIL_PASSWORD=your-app-password
STRIPE_API_KEY=sk_test_your_stripe_key
STRIPE_PUBLIC_KEY=pk_test_your_stripe_key
```

### 3. Application Configuration

Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/movie_ticket_db
    username: root
    password: your-password
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

server:
  port: 8080
  servlet:
    context-path: /api

jwt:
  secret: ${JWT_SECRET}
  expiration: 86400000
```

### 4. Running the Backend

```bash
# Using Maven
mvn spring-boot:run

# Or build and run
mvn clean package
java -jar target/movie-ticket-system-1.0.0.jar
```

The backend will start on `http://localhost:8080/api`

## Frontend Setup (React)

### 1. Install Dependencies

```bash
npm install
```

### 2. API Configuration

The API endpoint is already configured in `src/config/api.ts`:

```typescript
const API_BASE_URL = "http://localhost:8080/api";
```

Change this if your backend is on a different port.

### 3. Start Development Server

```bash
npm run dev
```

The frontend will be available at `http://localhost:5173`

## Testing the Application

### Test Accounts

Create test users through the registration page or use your credentials.

### Test Movies

The backend should have some sample movies. If not, create them via admin endpoints.

### Test Booking Flow

1. **Home Page**: Browse movies
2. **Movie Detail**: View movie details and available showtimes
3. **Login/Register**: Create an account (auto-redirects to login)
4. **Select Seats**: Click on available seats
5. **Payment**: Use test card (any valid format)
6. **Confirmation**: View booking confirmation

## Available Endpoints Summary

### User Management

```
POST   /auth/login
POST   /auth/register
```

### Movies

```
GET    /movies
GET    /movies/{id}
GET    /movies/search?query=
GET    /movies/genre/{genre}
GET    /movies/upcoming
POST   /movies (Admin)
PUT    /movies/{id} (Admin)
DELETE /movies/{id} (Admin)
```

### Showtimes

```
GET    /showtimes/{id}
GET    /showtimes/movie/{movieId}
GET    /showtimes/movie/{movieId}/upcoming
GET    /showtimes/theater/{theaterId}
POST   /showtimes (Admin)
PUT    /showtimes/{id}/status/{status} (Admin)
DELETE /showtimes/{id} (Admin)
```

### Bookings

```
POST   /bookings (Authenticated)
GET    /bookings/{id} (Authenticated)
GET    /bookings/reference/{reference} (Authenticated)
GET    /bookings/user/my-bookings (Authenticated)
GET    /bookings/user/upcoming (Authenticated)
GET    /bookings/user/completed (Authenticated)
DELETE /bookings/{id} (Authenticated)
```

### Seats

```
GET    /seats/screen/{screenId}
GET    /seats/screen/{screenId}/available
POST   /seats/screen/{screenId}/initialize (Admin)
```

### Reviews

```
POST   /reviews (Authenticated)
GET    /reviews/{id}
GET    /reviews/movie/{movieId}
GET    /reviews/user/{userId} (Authenticated)
GET    /reviews/movie/{movieId}/stats
PUT    /reviews/{id} (Authenticated)
DELETE /reviews/{id} (Authenticated)
```

### Payments

```
POST   /payments/process (Authenticated)
GET    /payments/{id} (Admin)
GET    /payments/booking/{bookingId} (Authenticated)
POST   /payments/{id}/refund (Admin)
```

### Admin Dashboard

```
GET    /admin/dashboard/metrics (Admin)
GET    /admin/dashboard/report/daily?date=2024-01-01 (Admin)
GET    /admin/dashboard/report/monthly?year=2024&month=1 (Admin)
```

## Project Structure

```
movie-ticket-system/
├── src/
│   ├── main/
│   │   ├── java/com/movieticket/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST endpoints
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── repository/      # Spring Data repositories
│   │   │   ├── security/        # JWT & security
│   │   │   ├── service/         # Business logic
│   │   │   └── util/            # Utility classes
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── pom.xml
└── README.md

movie-ticket-frontend/
├── src/
│   ├── components/              # Reusable components
│   ├── config/                  # API configuration
│   ├── pages/                   # Page components
│   ├── services/                # API services
│   ├── store/                   # Zustand store
│   ├── styles/                  # CSS files
│   ├── App.tsx
│   ├── main.tsx
│   └── index.css
├── public/                      # Static files
├── package.json
├── vite.config.ts
└── tsconfig.json
```

## Troubleshooting

### Backend Issues

**Port already in use**:

```bash
# Windows - Find and kill process on port 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti :8080 | xargs kill -9
```

**Database connection error**:

- Verify MySQL is running
- Check credentials in `application.yml`
- Ensure database exists

**CORS errors**:

- Check `@CrossOrigin` annotations in controllers
- Verify frontend URL is allowed

### Frontend Issues

**Dependencies not installing**:

```bash
rm -rf node_modules package-lock.json
npm install
```

**API not connecting**:

- Check backend is running
- Verify API URL in `src/config/api.ts`
- Check browser console for errors
- Ensure backend has CORS enabled

**Blank page**:

- Clear browser cache
- Check console for JavaScript errors
- Verify React is loading

## Development Commands

### Backend

```bash
# Build
mvn clean package

# Run tests
mvn test

# Development mode with hot reload
mvn spring-boot:run

# Build and run
mvn clean package -DskipTests
java -jar target/movie-ticket-system-1.0.0.jar
```

### Frontend

```bash
# Development
npm run dev

# Build
npm run build

# Preview production build
npm run preview

# Lint
npm run lint

# Format code
npx prettier --write src/
```

## Production Deployment

### Backend

```bash
mvn clean package -DskipTests
java -jar target/movie-ticket-system-1.0.0.jar --spring.profiles.active=prod
```

### Frontend

```bash
npm run build
# Serve the dist folder with a web server (Nginx, Apache, etc.)
```

## Security Checklist

- [ ] Update JWT secret in production
- [ ] Use HTTPS for all communications
- [ ] Set secure cookies for sensitive data
- [ ] Implement rate limiting
- [ ] Validate all inputs on backend
- [ ] Use Stripe tokenization for real payments
- [ ] Implement CORS properly (no \* in production)
- [ ] Set security headers
- [ ] Use environment variables for secrets

## Next Steps

1. **Add Sample Data**: Create movies, theaters, and showtimes
2. **Customize Branding**: Update colors and logo
3. **Integrate Real Stripe**: Add Stripe keys for real payments
4. **Deploy**: Push to production servers
5. **Monitor**: Set up logging and error tracking

## Support & Documentation

- Spring Boot Docs: https://spring.io/projects/spring-boot
- React Docs: https://react.dev
- Zustand Docs: https://github.com/pmndrs/zustand
- React Router Docs: https://reactrouter.com
- Vite Docs: https://vitejs.dev

## Contact

For issues, please check the README files in each directory or contact the development team.
