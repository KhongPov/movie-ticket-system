import api from "../config/api";

export interface BookingDTO {
  id: number;
  bookingReference: string;
  showtimeId: number;
  movieTitle: string;
  theaterName: string;
  showDateTime: string;
  numberOfTickets: number;
  totalAmount: number;
  status: string;
  createdAt: string;
  tickets: BookingTicketDTO[];
}

export interface BookingTicketDTO {
  id: number;
  seatNumber: string;
  seatType: string;
  price: number;
}

export interface CreateBookingRequest {
  showtimeId: number;
  seatIds: number[];
}

export const bookingService = {
  createBooking: async (data: CreateBookingRequest): Promise<BookingDTO> => {
    const response = await api.post<BookingDTO>("/bookings", data);
    return response.data;
  },

  getBookingById: async (id: number): Promise<BookingDTO> => {
    const response = await api.get<BookingDTO>(`/bookings/${id}`);
    return response.data;
  },

  getBookingByReference: async (reference: string): Promise<BookingDTO> => {
    const response = await api.get<BookingDTO>(
      `/bookings/reference/${reference}`
    );
    return response.data;
  },

  getMyBookings: async (): Promise<BookingDTO[]> => {
    const response = await api.get<BookingDTO[]>("/bookings/user/my-bookings");
    return response.data;
  },

  getUpcomingBookings: async (): Promise<BookingDTO[]> => {
    const response = await api.get<BookingDTO[]>("/bookings/user/upcoming");
    return response.data;
  },

  getCompletedBookings: async (): Promise<BookingDTO[]> => {
    const response = await api.get<BookingDTO[]>("/bookings/user/completed");
    return response.data;
  },

  cancelBooking: async (id: number): Promise<void> => {
    await api.delete(`/bookings/${id}`);
  },
};
