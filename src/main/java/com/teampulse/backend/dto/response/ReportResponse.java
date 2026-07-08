package com.teampulse.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {
    private Long id;
    private Long userId;
    private String userFullName;
    private Long projectId;
    private String projectName;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private String tasksCompleted;
    private String tasksPlannedNext;
    private String blockers;
    private BigDecimal hoursWorked;
    private String notesLinks;
    private String status;
    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;
}
