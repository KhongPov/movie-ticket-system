import api from "../config/api";

export interface ReviewDTO {
  id: number;
  movieId: number;
  userId: number;
  rating: number;
  comment: string;
  createdAt: string;
  userName: string;
}

export interface MovieReviewStatsDTO {
  movieId: number;
  averageRating: number;
  totalReviews: number;
  ratingDistribution: {
    [key: number]: number;
  };
}

export interface CreateReviewRequest {
  movieId: number;
  rating: number;
  comment: string;
}

export const reviewService = {
  createReview: async (data: CreateReviewRequest): Promise<ReviewDTO> => {
    const response = await api.post<ReviewDTO>("/reviews", data);
    return response.data;
  },

  getReviewById: async (id: number): Promise<ReviewDTO> => {
    const response = await api.get<ReviewDTO>(`/reviews/${id}`);
    return response.data;
  },

  getReviewsByMovie: async (movieId: number): Promise<ReviewDTO[]> => {
    const response = await api.get<ReviewDTO[]>(`/reviews/movie/${movieId}`);
    return response.data;
  },

  getReviewsByUser: async (userId: number): Promise<ReviewDTO[]> => {
    const response = await api.get<ReviewDTO[]>(`/reviews/user/${userId}`);
    return response.data;
  },

  getMovieReviewStats: async (
    movieId: number
  ): Promise<MovieReviewStatsDTO> => {
    const response = await api.get<MovieReviewStatsDTO>(
      `/reviews/movie/${movieId}/stats`
    );
    return response.data;
  },

  updateReview: async (
    id: number,
    data: CreateReviewRequest
  ): Promise<ReviewDTO> => {
    const response = await api.put<ReviewDTO>(`/reviews/${id}`, data);
    return response.data;
  },

  deleteReview: async (id: number): Promise<void> => {
    await api.delete(`/reviews/${id}`);
  },
};
