import { create } from "zustand";
import type { AuthResponse } from "../services/authService";
import { authService } from "../services/authService";
import { setAuthToken } from "../config/api";

interface User {
  email: string;
  firstName: string;
  lastName: string;
}

export interface AuthStore {
  user: User | null;
  isAuthenticated: boolean;
  hydrated: boolean;
  login: (response: AuthResponse) => void;
  logout: () => Promise<void>;
  hydrate: () => Promise<void>;
}

const mapResponseToUser = (response: AuthResponse): User => ({
  email: response.email,
  firstName: response.firstName,
  lastName: response.lastName,
});

export const useAuthStore = create<AuthStore>((set) => ({
  user: null,
  isAuthenticated: false,
  hydrated: false,

  login: (response: AuthResponse) => {
    const user = mapResponseToUser(response);
    if (response.token) {
      setAuthToken(response.token);
    }
    set({
      user,
      isAuthenticated: true,
    });
  },

  logout: async () => {
    try {
      await authService.logout();
    } finally {
      setAuthToken(undefined);
      set({
        user: null,
        isAuthenticated: false,
      });
    }
  },

  hydrate: async () => {
    try {
      const profile = await authService.getCurrentUser();
      set({
        user: mapResponseToUser(profile),
        isAuthenticated: true,
        hydrated: true,
      });
    } catch {
      setAuthToken(undefined);
      set({
        user: null,
        isAuthenticated: false,
        hydrated: true,
      });
    }
  },
}));
