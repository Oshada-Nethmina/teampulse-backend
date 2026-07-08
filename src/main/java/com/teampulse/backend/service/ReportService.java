package com.teampulse.backend.service;

import com.teampulse.backend.dto.request.ReportRequest;
import com.teampulse.backend.dto.response.ReportResponse;
import com.teampulse.backend.entity.WeeklyReport;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    ReportResponse createReport(Long userId, ReportRequest reportRequest);
    ReportResponse updateReport(Long userId, Long reportId, ReportRequest reportRequest);
    List<ReportResponse> getUserReports(Long userId);
    ReportResponse getReportById(Long userId, Long reportId);
    List<ReportResponse> searchReports(Long userId,
                                       Long projectId,
                                       LocalDate from,
                                       LocalDate to);
    List<WeeklyReport> searchRawReports(Long userId,
                                        Long projectId,
                                        LocalDate from,
                                        LocalDate to);
}

