package com.teampulse.backend.controller;

import com.teampulse.backend.dto.response.DashboardSummaryResponse;
import com.teampulse.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<DashboardSummaryResponse> getDashboardSummary(
            @RequestParam(required = false)
            Long projectId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate weekStartDate) {

        return ResponseEntity.ok(
                dashboardService.getDashboardSummary(projectId, weekStartDate));
    }
}
