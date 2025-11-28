package com.movieticket.service;

import com.movieticket.dto.DashboardDTO;
import com.movieticket.dto.ReportDTO;
import com.movieticket.entity.*;
import com.movieticket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private ScreenRepository screenRepository;

    public DashboardDTO getDashboardMetrics() {

        Long totalUsers = userRepository.count();
        Long totalMovies = movieRepository.count();
        Long totalShowtimes = (long) showtimeRepository.findAll().size();

        List<Booking> allBookings = bookingRepository.findAll();
        Long totalBookings = (long) allBookings.size();

        Long completedBookings = allBookings.stream()
                .filter(b -> b.getStatus().equals("COMPLETED"))
                .count();

        Long cancelledBookings = allBookings.stream()
                .filter(b -> b.getStatus().equals("CANCELLED"))
                .count();

        Long totalRevenue = allBookings.stream()
                .filter(b -> b.getStatus().equals("COMPLETED"))
                .mapToLong(b -> Math.round(b.getTotalAmount()))
                .sum();

        Double averageBookingValue = completedBookings > 0
                ? totalRevenue.doubleValue() / completedBookings
                : 0.0;

        Long totalTheaters = theaterRepository.count();
        Long totalScreens = screenRepository.count();

        return DashboardDTO.builder()
                .totalUsers(totalUsers)
                .totalMovies(totalMovies)
                .totalShowtimes(totalShowtimes)
                .totalBookings(totalBookings)
                .totalRevenue(totalRevenue)
                .completedBookings(completedBookings)
                .cancelledBookings(cancelledBookings)
                .averageBookingValue(averageBookingValue)
                .totalTheaters(totalTheaters)
                .totalScreens(totalScreens)
                .build();
    }

    public ReportDTO generateDailyReport(LocalDate date) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<Payment> dailyPayments = paymentRepository.findPaymentsByStatusAndDateRange(
                "SUCCESS", startOfDay, endOfDay);

        Long totalRevenue = dailyPayments.stream()
                .mapToLong(p -> Math.round(p.getAmount()))
                .sum();

        List<Booking> dailyBookings = bookingRepository.findAll().stream()
                .filter(b -> {
                    LocalDateTime createdAt = b.getCreatedAt();
                    return !createdAt.isBefore(startOfDay) && createdAt.isBefore(endOfDay);
                })
                .toList();

        Long completedBookings = dailyBookings.stream()
                .filter(b -> b.getStatus().equals("COMPLETED"))
                .count();

        Long cancelledBookings = dailyBookings.stream()
                .filter(b -> b.getStatus().equals("CANCELLED"))
                .count();

        Double averageBookingValue = completedBookings > 0
                ? totalRevenue.doubleValue() / completedBookings
                : 0.0;

        return ReportDTO.builder()
                .totalBookings((long) dailyBookings.size())
                .totalRevenue(totalRevenue)
                .totalUsers(userRepository.count())
                .totalMovies(movieRepository.count())
                .averageBookingValue(averageBookingValue)
                .reportDate(date)
                .completedBookings(completedBookings)
                .cancelledBookings(cancelledBookings)
                .activeMovies((long) movieRepository.findByStatus("ACTIVE").size())  // FIXED
                .build();
    }

    public ReportDTO generateMonthlyReport(int year, int month) {

        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1);

        LocalDateTime startOfMonthDt = startOfMonth.atStartOfDay();
        LocalDateTime endOfMonthDt = endOfMonth.atStartOfDay();

        List<Payment> monthlyPayments = paymentRepository.findPaymentsByStatusAndDateRange(
                "SUCCESS", startOfMonthDt, endOfMonthDt);

        Long totalRevenue = monthlyPayments.stream()
                .mapToLong(p -> Math.round(p.getAmount()))
                .sum();

        List<Booking> monthlyBookings = bookingRepository.findAll().stream()
                .filter(b -> {
                    LocalDateTime createdAt = b.getCreatedAt();
                    return !createdAt.isBefore(startOfMonthDt) && createdAt.isBefore(endOfMonthDt);
                })
                .toList();

        Long completedBookings = monthlyBookings.stream()
                .filter(b -> b.getStatus().equals("COMPLETED"))
                .count();

        Long cancelledBookings = monthlyBookings.stream()
                .filter(b -> b.getStatus().equals("CANCELLED"))
                .count();

        Double averageBookingValue = completedBookings > 0
                ? totalRevenue.doubleValue() / completedBookings
                : 0.0;

        return ReportDTO.builder()
                .totalBookings((long) monthlyBookings.size())
                .totalRevenue(totalRevenue)
                .totalUsers(userRepository.count())
                .totalMovies(movieRepository.count())
                .averageBookingValue(averageBookingValue)
                .reportDate(startOfMonth)
                .completedBookings(completedBookings)
                .cancelledBookings(cancelledBookings)
                .activeMovies((long) movieRepository.findByStatus("ACTIVE").size())  // FIXED
                .build();
    }
}
