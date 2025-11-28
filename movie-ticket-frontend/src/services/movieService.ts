import api from "../config/api";

export interface MovieDTO {
  id: number;
  title: string;
  description: string;
  genre: string;
  duration: number;
  releaseDate: string;
  posterUrl: string;
  rating: number;
  status: string;
}

export interface CreateMovieRequest {
  title: string;
  description: string;
  genre: string;
  duration: number;
  releaseDate: string;
  posterUrl: string;
}

export const movieService = {
  getAllMovies: async (): Promise<MovieDTO[]> => {
    const response = await api.get<MovieDTO[]>("/movies");
    return response.data;
  },

  getMovieById: async (id: number): Promise<MovieDTO> => {
    const response = await api.get<MovieDTO>(`/movies/${id}`);
    return response.data;
  },

  searchMovies: async (query: string): Promise<MovieDTO[]> => {
    const response = await api.get<MovieDTO[]>("/movies/search", {
      params: { query },
    });
    return response.data;
  },

  getMoviesByGenre: async (genre: string): Promise<MovieDTO[]> => {
    const response = await api.get<MovieDTO[]>(`/movies/genre/${genre}`);
    return response.data;
  },

  getUpcomingMovies: async (): Promise<MovieDTO[]> => {
    const response = await api.get<MovieDTO[]>("/movies/upcoming");
    return response.data;
  },

  createMovie: async (data: CreateMovieRequest): Promise<MovieDTO> => {
    const response = await api.post<MovieDTO>("/movies", data);
    return response.data;
  },

  updateMovie: async (
    id: number,
    data: CreateMovieRequest
  ): Promise<MovieDTO> => {
    const response = await api.put<MovieDTO>(`/movies/${id}`, data);
    return response.data;
  },

  deleteMovie: async (id: number): Promise<void> => {
    await api.delete(`/movies/${id}`);
  },
};
