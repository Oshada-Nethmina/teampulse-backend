package com.teampulse.backend.service.impl;

import com.teampulse.backend.dto.response.DashboardSummaryResponse;
import com.teampulse.backend.dto.response.ReportResponse;
import com.teampulse.backend.dto.response.SubmissionStatusResponse;
import com.teampulse.backend.entity.User;
import com.teampulse.backend.entity.WeeklyReport;
import com.teampulse.backend.enums.ReportStatus;
import com.teampulse.backend.enums.Role;
import com.teampulse.backend.repository.UserRepository;
import com.teampulse.backend.repository.WeeklyReportRepository;
import com.teampulse.backend.service.DashboardService;
import com.teampulse.backend.specification.WeeklyReportSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {
    private final WeeklyReportRepository reportRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter WEEK_LABEL =
            DateTimeFormatter.ofPattern("MMM d");

    @Override
    public DashboardSummaryResponse getDashboardSummary(Long projectId, LocalDate weekStartDate) {
        LocalDate targetWeek = weekStartDate != null
                ? weekStartDate
                : getCurrentWeekStart();

        List<User> teamMembers = userRepository.findByRole(Role.TEAM_MEMBER);
        List<WeeklyReport> weeklyReports = reportRepository.findByWeekStartDate(targetWeek);

        long submittedReports = weeklyReports.stream()
                .filter(r -> r.getStatus() == ReportStatus.SUBMITTED)
                .count();

        double complianceRate =
                calculateComplianceRate(submittedReports, teamMembers.size());

        long openBlockers = reportRepository.findOpenBlockersForWeek(targetWeek).size();

        List<SubmissionStatusResponse> submissionStatus = getSubmissionStatus(teamMembers, weeklyReports, targetWeek);

        Map<String, Long> workloadByProject =
                getProjectWorkload(targetWeek);

        Map<String, Double> tasksCompletedTrend =
                getTasksCompletedTrend(targetWeek);

        List<ReportResponse> recentActivity =
                getRecentActivity();

        return DashboardSummaryResponse.builder()
                .totalReportsSubmitted(submittedReports)
                .totalTeamMembers(teamMembers.size())
                .complianceRatePercent(roundToOneDecimal(complianceRate))
                .openBlockersCount(openBlockers)
                .submissionStatusByMember(submissionStatus)
                .workloadByProject(workloadByProject)
                .tasksCompletedTrend(tasksCompletedTrend)
                .recentActivity(recentActivity)
                .build();
    }


    private List<SubmissionStatusResponse> getSubmissionStatus(List<User> teamMembers, List<WeeklyReport> weeklyReports, LocalDate targetWeek) {

        Map<Long, WeeklyReport> reportsByUser = weeklyReports.stream()
                .collect(Collectors.toMap(
                        report -> report.getUser().getUserId(),
                        report -> report,
                        (first, second) -> first));

        boolean weekIsPast = targetWeek.plusDays(6).isBefore(LocalDate.now());

        List<SubmissionStatusResponse> submissionStatus = new ArrayList<>();

        for (User member :teamMembers) {

            WeeklyReport report = reportsByUser.get(member.getUserId());

            String status;
            LocalDateTime submittedAt = null;

            if (report != null && report.getStatus() == ReportStatus.SUBMITTED) {
                status = "SUBMITTED";
                submittedAt = report.getSubmittedAt();
            } else if (weekIsPast) {
                status = "LATE";
            } else {
                status = "PENDING";
            }

            submissionStatus.add(
                    new SubmissionStatusResponse(
                            member.getUserId(),
                            member.getFullName(),
                            status,
                            submittedAt));
        }

        return submissionStatus;
    }

    private List<ReportResponse> getRecentActivity() {

        return reportRepository.findTop20ByOrderByUpdatedAtDesc()
                .stream()
                .map(this::mapToReportResponse)
                .toList();
    }

    private double calculateComplianceRate(long submittedReports,
                                           int totalMembers) {

        if (totalMembers == 0) {
            return 0.0;
        }

        return (submittedReports * 100.0) / totalMembers;
    }


    private double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private Map<String, Long> getProjectWorkload(LocalDate targetWeek) {

        List<WeeklyReport> reports = reportRepository.findAll(
                WeeklyReportSpecification.filter(
                        null,
                        null,
                        targetWeek.minusWeeks(11),
                        targetWeek.plusDays(6)
                )
        );

        return reports.stream()
                .filter(report -> report.getProject() != null)
                .collect(Collectors.groupingBy(
                        report -> report.getProject().getName(),
                        Collectors.counting()
                ));
    }

    private Map<String, Double> getTasksCompletedTrend(LocalDate targetWeek) {

        Map<String, Double> trend = new LinkedHashMap<>();

        for (int i = 11; i >= 0; i--) {

            LocalDate weekStart = targetWeek.minusWeeks(i);

            long completedReports = reportRepository.findByWeekStartDate(weekStart)
                    .stream()
                    .filter(report -> report.getStatus() == ReportStatus.SUBMITTED)
                    .count();

            trend.put(
                    weekStart.format(WEEK_LABEL),
                    (double) completedReports);
        }

        return trend;
    }


    private ReportResponse mapToReportResponse(WeeklyReport report) {

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

    private LocalDate getCurrentWeekStart() {

        LocalDate today = LocalDate.now();

        return today.minusDays(today.getDayOfWeek().getValue() - 1L);
    }


}
