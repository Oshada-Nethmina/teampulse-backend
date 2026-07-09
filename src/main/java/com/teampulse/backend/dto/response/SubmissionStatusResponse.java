package com.teampulse.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionStatusResponse {
    private Long userId;
    private String userFullName;
    private String status; // SUBMITTED / PENDING / LATE
    private LocalDateTime submittedAt;
}
