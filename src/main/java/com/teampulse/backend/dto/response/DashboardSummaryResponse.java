package com.teampulse.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummaryResponse {
    private long totalReportsSubmitted;
    private long totalTeamMembers;
    private double complianceRatePercent;
    private long openBlockersCount;

    private List<SubmissionStatusResponse> submissionStatusByMember;
    private Map<String, Long> workloadByProject;          // project name -> report count
    private Map<String, Double> tasksCompletedTrend;       // week label -> count of reports with tasks
    private List<ReportResponse> recentActivity;
}
