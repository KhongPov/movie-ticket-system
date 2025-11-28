import api from "../config/api";

export interface PaymentRequest {
  bookingId: number;
  paymentMethod: string;
  stripeToken?: string;
}

export interface PaymentResponse {
  paymentId: number;
  bookingReference: string;
  amount: number;
  status: string;
  transactionId: string;
  message?: string;
}

export const paymentService = {
  processPayment: async (data: PaymentRequest): Promise<PaymentResponse> => {
    const response = await api.post<PaymentResponse>("/payments/process", data);
    return response.data;
  },

  getPaymentStatus: async (id: number): Promise<PaymentResponse> => {
    const response = await api.get<PaymentResponse>(`/payments/${id}`);
    return response.data;
  },

  getPaymentByBooking: async (bookingId: number): Promise<PaymentResponse> => {
    const response = await api.get<PaymentResponse>(
      `/payments/booking/${bookingId}`
    );
    return response.data;
  },

  refundPayment: async (id: number): Promise<PaymentResponse> => {
    const response = await api.post<PaymentResponse>(`/payments/${id}/refund`);
    return response.data;
  },
};
