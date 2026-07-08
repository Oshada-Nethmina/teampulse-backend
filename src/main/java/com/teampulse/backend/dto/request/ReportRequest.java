package com.teampulse.backend.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequest {
    @NotNull(message = "Week start date is required")
    private LocalDate weekStartDate;

    @NotNull(message = "Week end date is required")
    private LocalDate weekEndDate;

    private Long projectId;

    @NotBlank(message = "Tasks completed is required")
    private String tasksCompleted;

    @NotBlank(message = "Tasks planned for next week is required")
    private String tasksPlannedNext;

    private String blockers;

    @DecimalMin(value = "0.0", message = "Hours worked cannot be negative")
    @DecimalMax(value = "168.0", message = "Hours worked cannot exceed 168 in a week")
    private BigDecimal hoursWorked;

    private String notesLinks;

    // true => submit immediately, false/null => save as draft
    private Boolean submit;
}
