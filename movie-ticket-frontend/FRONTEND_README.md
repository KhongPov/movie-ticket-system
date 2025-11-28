# Movie Ticket System - Frontend

A modern React + TypeScript web application for booking movie tickets with Stripe payment integration.

## Features

- **User Authentication**: Register and login with JWT tokens
- **Movie Browsing**: Browse and search movies by title or genre
- **Movie Details**: View detailed information about movies with ratings and reviews
- **Seat Selection**: Interactive seat selection for showtimes
- **Booking Management**: View and manage your bookings
- **Reviews & Ratings**: Leave and view movie reviews
- **Payment Integration**: Secure payment processing
- **Admin Dashboard**: Admin features for managing movies and showtimes (pending)

## Tech Stack

- **Frontend Framework**: React 19 with TypeScript
- **Routing**: React Router v6
- **State Management**: Zustand
- **HTTP Client**: Axios
- **Styling**: CSS3 with custom design system
- **Build Tool**: Vite

## Project Structure

```
src/
├── components/        # Reusable React components
├── config/           # Configuration files (API setup)
├── pages/            # Page components
├── services/         # API service calls
├── store/            # Zustand store (state management)
├── styles/           # CSS stylesheets
├── App.tsx           # Main App component
├── main.tsx          # Application entry point
└── index.css         # Global styles
```

## Getting Started

### Prerequisites

- Node.js (v16 or higher)
- npm or yarn

### Installation

1. **Install dependencies**:

```bash
npm install
```

2. **Configure API endpoint** (optional):
   Edit `src/config/api.ts` if your backend runs on a different port:

```typescript
const API_BASE_URL = "http://localhost:8080/api";
```

### Running the Development Server

```bash
npm run dev
```

The app will be available at `http://localhost:5173`

### Building for Production

```bash
npm run build
```

The optimized build will be in the `dist/` directory.

### Preview Production Build

```bash
npm run preview
```

## API Endpoints

### Authentication

- `POST /auth/login` - User login
- `POST /auth/register` - User registration

### Movies

- `GET /movies` - Get all active movies
- `GET /movies/{id}` - Get movie details
- `GET /movies/search?query=` - Search movies
- `GET /movies/genre/{genre}` - Get movies by genre
- `GET /movies/upcoming` - Get upcoming movies

### Showtimes

- `GET /showtimes/{id}` - Get showtime details
- `GET /showtimes/movie/{movieId}` - Get showtimes for a movie
- `GET /showtimes/movie/{movieId}/upcoming` - Get upcoming showtimes
- `GET /showtimes/{showtimeId}/available-seats` - Get available seats

### Bookings

- `POST /bookings` - Create a booking
- `GET /bookings/user/my-bookings` - Get user's bookings
- `GET /bookings/user/upcoming` - Get upcoming bookings
- `GET /bookings/user/completed` - Get completed bookings
- `DELETE /bookings/{id}` - Cancel booking

### Seats

- `GET /seats/screen/{screenId}` - Get screen seats
- `GET /seats/screen/{screenId}/available` - Get available seats

### Reviews

- `POST /reviews` - Create a review
- `GET /reviews/movie/{movieId}` - Get movie reviews
- `GET /reviews/movie/{movieId}/stats` - Get review statistics

### Payments

- `POST /payments/process` - Process payment
- `GET /payments/{id}` - Get payment status
- `GET /payments/booking/{bookingId}` - Get payment by booking

## Pages

### Home Page (`/`)

- Display all active movies
- Search functionality
- Genre filtering
- Movie grid with hover effects

### Movie Detail Page (`/movie/:id`)

- Full movie information
- Available showtimes
- Customer reviews and ratings
- Review submission form (authenticated users only)

### Booking Page (`/booking/:showtimeId`)

- Interactive seat selection with visual feedback
- Real-time price calculation
- Seat availability display
- Booking summary

### Payment Page (`/payment/:bookingId`)

- Secure payment form
- Card information input
- Payment status confirmation

### My Bookings Page (`/my-bookings`)

- View all user bookings
- Filter by status (All, Upcoming, Completed)
- Cancel pending bookings
- Booking details and receipt

### Login Page (`/login`)

- User authentication
- Form validation
- Error handling

### Register Page (`/register`)

- New user registration
- Password validation
- Auto-login after registration

## Authentication Flow

1. User registers or logs in.
2. Backend issues a JWT, stores it inside a secure HTTP-only cookie, and returns basic user info.
3. Cookie is automatically attached to every API request (no `localStorage` usage).
4. Frontend hydrates auth state via `/auth/me` to survive refreshes.
5. When the cookie expires or becomes invalid, protected routes redirect back to `/login`.

## Styling

The application uses a custom design system with:

- **Primary Color**: Netflix-inspired red (#e50914)
- **Dark Theme**: Professional dark background
- **Responsive Design**: Mobile-first approach
- **Smooth Animations**: Hover effects and transitions

### Color Palette

```css
--primary-color: #e50914       /* Main action color */
--primary-dark: #b80812        /* Dark variant */
--secondary-color: #221f1f     /* Dark backgrounds */
--light-color: #f5f5f5         /* Text color */
--dark-color: #1a1a1a          /* Darkest backgrounds */
--success-color: #22c55e        /* Success states */
--error-color: #ef4444          /* Error states */
--warning-color: #f59e0b        /* Warning states */
```

## State Management with Zustand

The `useAuthStore` manages user authentication state and hydration:

```typescript
const { user, isAuthenticated, hydrate, logout } = useAuthStore();
```

## Error Handling

- API errors are caught and displayed to users
- Failed token validation redirects to login
- Network errors are logged with user feedback
- Form validation provides real-time feedback

## Security Considerations

- JWT is stored inside an HTTP-only, same-site cookie to mitigate XSS token theft.
- CORS is restricted to `http://localhost:5173` with credentials enabled.
- Form inputs are validated before submission.
- Payment data should use Stripe tokenization (currently demo).

## Troubleshooting

### API Connection Issues

- Ensure backend is running on `http://localhost:8080`
- Check CORS configuration in Spring Boot backend
- Verify `API_BASE_URL` in `src/config/api.ts`

### Token Expiration

- Clear browser cookies for the site and refresh the page.
- Log in again with your credentials.

### Seat Selection Not Working

- Ensure JavaScript is enabled
- Check browser console for errors
- Verify showtime has available seats

## Future Enhancements

- [ ] Admin dashboard for managing movies
- [ ] Email confirmation for bookings
- [ ] Stripe live payment integration
- [ ] User profile management
- [ ] Wishlist functionality
- [ ] Social sharing for movies
- [ ] Movie recommendations

## License

This project is part of a Movie Ticket System assignment.

## Support

For issues or questions, contact the development team.
