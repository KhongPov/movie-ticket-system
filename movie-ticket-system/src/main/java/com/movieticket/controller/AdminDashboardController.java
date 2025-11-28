package com.movieticket.controller;

import com.movieticket.dto.DashboardDTO;
import com.movieticket.dto.ReportDTO;
import com.movieticket.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/dashboard")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/metrics")
    public ResponseEntity<DashboardDTO> getDashboardMetrics() {
        return ResponseEntity.ok(reportService.getDashboardMetrics());
    }

    @GetMapping("/report/daily")
    public ResponseEntity<ReportDTO> getDailyReport(@RequestParam String date) {
        LocalDate reportDate = LocalDate.parse(date);
        return ResponseEntity.ok(reportService.generateDailyReport(reportDate));
    }

    @GetMapping("/report/monthly")
    public ResponseEntity<ReportDTO> getMonthlyReport(@RequestParam int year, @RequestParam int month) {
        return ResponseEntity.ok(reportService.generateMonthlyReport(year, month));
    }
}
