package com.teampulse.backend.service.impl;

import com.teampulse.backend.dto.request.ReportRequest;
import com.teampulse.backend.dto.response.ReportResponse;
import com.teampulse.backend.entity.Project;
import com.teampulse.backend.entity.User;
import com.teampulse.backend.entity.WeeklyReport;
import com.teampulse.backend.enums.ReportStatus;
import com.teampulse.backend.exception.BadRequestException;
import com.teampulse.backend.exception.ResourceNotFoundException;
import com.teampulse.backend.repository.ProjectRepository;
import com.teampulse.backend.repository.UserRepository;
import com.teampulse.backend.repository.WeeklyReportRepository;
import com.teampulse.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final WeeklyReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public ReportResponse createReport(Long userId, ReportRequest reportRequest) {
        validateWeekDates(reportRequest);

        User user = getUserById(userId);

        WeeklyReport report = reportRepository
                .findByUser_UserIdAndWeekStartDate(userId, reportRequest.getWeekStartDate())
                .orElseGet(() -> WeeklyReport.builder()
                        .user(user)
                        .build());

        Project project = getProjectById(reportRequest.getProjectId());

        updateReportFields(report, reportRequest, project);

        if (Boolean.TRUE.equals(reportRequest.getSubmit())) {
            report.setStatus(ReportStatus.SUBMITTED);
            report.setSubmittedAt(LocalDateTime.now());
        } else {
            report.setStatus(ReportStatus.DRAFT);
        }

        return mapToResponse(reportRepository.save(report));
    }

    @Override
    @Transactional
    public ReportResponse updateReport(Long userId, Long reportId, ReportRequest reportRequest) {

        validateWeekDates(reportRequest);

        WeeklyReport report = getReportEntityById(reportId);

        if (!report.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("You can only edit your own reports");
        }

        Project project = getProjectById(reportRequest.getProjectId());

        updateReportFields(report, reportRequest, project);

        if (Boolean.TRUE.equals(reportRequest.getSubmit())) {
            report.setStatus(ReportStatus.SUBMITTED);
            report.setSubmittedAt(LocalDateTime.now());
        }

        return mapToResponse(reportRepository.save(report));
    }


    @Override
    public List<ReportResponse> getUserReports(Long userId) {
        return reportRepository.findByUser_UserIdOrderByWeekStartDateDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ReportResponse getReportById(Long userId, Long reportId) {
        WeeklyReport report = getReportEntityById(reportId);

        if (!report.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("You can only view your own reports");
        }

        return mapToResponse(report);
    }

    @Override
    public List<ReportResponse> searchReports(Long userId, Long projectId, LocalDate from, LocalDate to) {
        return reportRepository.search(userId, projectId, from, to)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<WeeklyReport> searchRawReports(Long userId, Long projectId, LocalDate from, LocalDate to) {
        return reportRepository.search(userId, projectId, from, to);
    }

    private void validateWeekDates(ReportRequest reportRequest) {

        if (reportRequest.getWeekEndDate().isBefore(reportRequest.getWeekStartDate())) {
            throw new BadRequestException("Week end date cannot be before week start date");
        }
    }

    private User getUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }


    private Project getProjectById(Long projectId) {

        if (projectId == null) {
            return null;
        }

        return projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found: " + projectId));
    }

    private WeeklyReport getReportEntityById(Long reportId) {

        return reportRepository.findById(reportId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Report not found: " + reportId));
    }

    private void updateReportFields(WeeklyReport report,
                              ReportRequest request,
                              Project project) {

        report.setProject(project);
        report.setWeekStartDate(request.getWeekStartDate());
        report.setWeekEndDate(request.getWeekEndDate());
        report.setTasksCompleted(request.getTasksCompleted());
        report.setTasksPlannedNext(request.getTasksPlannedNext());
        report.setBlockers(request.getBlockers());
        report.setHoursWorked(request.getHoursWorked());
        report.setNotesLinks(request.getNotesLinks());
    }

    private ReportResponse mapToResponse(WeeklyReport report) {

        return ReportResponse.builder()
                .id(report.getId())
                .userId(report.getUser().getUserId())
                .userFullName(report.getUser().getFullName())
                .projectId(report.getProject() != null
                        ? report.getProject().getId()
                        : null)
                .projectName(report.getProject() != null
                        ? report.getProject().getName()
                        : null)
                .weekStartDate(report.getWeekStartDate())
                .weekEndDate(report.getWeekEndDate())
                .tasksCompleted(report.getTasksCompleted())
                .tasksPlannedNext(report.getTasksPlannedNext())
                .blockers(report.getBlockers())
                .hoursWorked(report.getHoursWorked())
                .notesLinks(report.getNotesLinks())
                .status(report.getStatus().name())
                .submittedAt(report.getSubmittedAt())
                .updatedAt(report.getUpdatedAt())
                .build();
    }
}
