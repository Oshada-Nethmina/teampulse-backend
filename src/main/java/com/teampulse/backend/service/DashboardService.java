package com.teampulse.backend.service;

import com.teampulse.backend.dto.response.DashboardSummaryResponse;

import java.time.LocalDate;

public interface DashboardService {
    DashboardSummaryResponse getDashboardSummary(Long projectId, LocalDate weekStartDate);

}
