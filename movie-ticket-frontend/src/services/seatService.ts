import api from "../config/api";

export interface SeatDTO {
  id: number;
  seatNumber: string;
  seatType: string;
  status: string;
}

export interface ScreenSeatsDTO {
  screenId: number;
  screenNumber: string;
  totalSeats: number;
  seats: SeatDTO[];
}

export const seatService = {
  getScreenSeats: async (screenId: number): Promise<ScreenSeatsDTO> => {
    const response = await api.get<ScreenSeatsDTO>(`/seats/screen/${screenId}`);
    return response.data;
  },

  getAvailableSeats: async (screenId: number): Promise<SeatDTO[]> => {
    const response = await api.get<SeatDTO[]>(
      `/seats/screen/${screenId}/available`
    );
    return response.data;
  },

  initializeScreenSeats: async (
    screenId: number,
    totalSeats: number
  ): Promise<void> => {
    await api.post(`/seats/screen/${screenId}/initialize`, null, {
      params: { totalSeats },
    });
  },
};
