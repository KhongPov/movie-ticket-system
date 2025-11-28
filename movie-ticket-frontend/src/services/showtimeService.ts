import api from "../config/api";

export interface ShowtimeDTO {
  id: number;
  movieId: number;
  theaterId: number;
  screenId: number;
  showDateTime: string;
  ticketPrice: number;
  status: string;
  movieTitle: string;
  theaterName: string;
  screenNumber: string;
  availableSeats: number;
}

export interface CreateShowtimeRequest {
  movieId: number;
  theaterId: number;
  screenId: number;
  showDateTime: string;
  ticketPrice: number;
}

export const showtimeService = {
  getShowtimeById: async (id: number): Promise<ShowtimeDTO> => {
    const response = await api.get<ShowtimeDTO>(`/showtimes/${id}`);
    return response.data;
  },

  getShowtimesByMovie: async (movieId: number): Promise<ShowtimeDTO[]> => {
    const response = await api.get<ShowtimeDTO[]>(
      `/showtimes/movie/${movieId}`
    );
    return response.data;
  },

  getUpcomingShowtimes: async (movieId: number): Promise<ShowtimeDTO[]> => {
    const response = await api.get<ShowtimeDTO[]>(
      `/showtimes/movie/${movieId}/upcoming`
    );
    return response.data;
  },

  getShowtimesByTheater: async (theaterId: number): Promise<ShowtimeDTO[]> => {
    const response = await api.get<ShowtimeDTO[]>(
      `/showtimes/theater/${theaterId}`
    );
    return response.data;
  },

  getAvailableSeats: async (showtimeId: number): Promise<number> => {
    const response = await api.get<number>(
      `/showtimes/${showtimeId}/available-seats`
    );
    return response.data;
  },

  createShowtime: async (data: CreateShowtimeRequest): Promise<ShowtimeDTO> => {
    const response = await api.post<ShowtimeDTO>("/showtimes", data);
    return response.data;
  },

  updateShowtimeStatus: async (id: number, status: string): Promise<void> => {
    await api.put(`/showtimes/${id}/status/${status}`);
  },

  cancelShowtime: async (id: number): Promise<void> => {
    await api.delete(`/showtimes/${id}`);
  },
};
