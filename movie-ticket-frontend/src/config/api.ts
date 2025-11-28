import axios from "axios";
import type { AxiosError } from "axios";

const API_BASE_URL = "http://localhost:8080/api";
const AUTH_TOKEN_KEY = "movie_ticket_auth_token";

const api = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

export const setAuthToken = (token?: string) => {
  if (typeof window === "undefined") {
    return;
  }

  if (token) {
    api.defaults.headers.common.Authorization = `Bearer ${token}`;
    localStorage.setItem(AUTH_TOKEN_KEY, token);
  } else {
    delete api.defaults.headers.common.Authorization;
    localStorage.removeItem(AUTH_TOKEN_KEY);
  }
};

if (typeof window !== "undefined") {
  const savedToken = localStorage.getItem(AUTH_TOKEN_KEY);
  if (savedToken) {
    setAuthToken(savedToken);
  }
}

api.interceptors.request.use((config) => {
  if (typeof window !== "undefined") {
    const token = localStorage.getItem(AUTH_TOKEN_KEY);
    if (token) {
      config.headers = config.headers ?? {};
      config.headers.Authorization = `Bearer ${token}`;
    }
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    if (error.response?.status === 401) {
      if (window.location.pathname !== "/login") {
        window.location.href = "/login";
      }
    }
    return Promise.reject(error);
  }
);

export default api;
