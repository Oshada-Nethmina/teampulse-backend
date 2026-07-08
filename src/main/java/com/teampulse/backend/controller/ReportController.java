package com.teampulse.backend.controller;

import com.teampulse.backend.dto.request.ReportRequest;
import com.teampulse.backend.dto.response.ReportResponse;
import com.teampulse.backend.security.UserPrincipal;
import com.teampulse.backend.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    // Create a new weekly report
    @PostMapping("/create-report")
    public ResponseEntity<ReportResponse> createReport(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ReportRequest reportRequest) {

        return ResponseEntity.ok(
                reportService.createReport(userPrincipal.getId(), reportRequest));
    }

    // Update an existing report
    @PutMapping("/update-report/{reportId}")
    public ResponseEntity<ReportResponse> updateReport(
            @PathVariable Long reportId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ReportRequest reportRequest) {

        return ResponseEntity.ok(
                reportService.updateReport(
                        userPrincipal.getId(),
                        reportId,
                        reportRequest));
    }

    // Get all reports of the logged-in user
    @GetMapping("/findAll")
    public ResponseEntity<List<ReportResponse>> getMyReports(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        return ResponseEntity.ok(
                reportService.getUserReports(userPrincipal.getId()));
    }

    // Get one report of the logged-in user
    @GetMapping("find-report/{reportId}")
    public ResponseEntity<ReportResponse> getReport(
            @PathVariable Long reportId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        return ResponseEntity.ok(
                reportService.getReportById(
                        userPrincipal.getId(),
                        reportId));
    }

    // Manager search reports
    @GetMapping("/search")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<ReportResponse>> searchReports(

            @RequestParam(required = false) Long userId,

            @RequestParam(required = false) Long projectId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to) {

        return ResponseEntity.ok(
                reportService.searchReports(
                        userId,
                        projectId,
                        from,
                        to));
    }
}
