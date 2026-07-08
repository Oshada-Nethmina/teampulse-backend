package com.teampulse.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRequest {
    @NotBlank(message = "Project name is required")
    @Size(max = 150)
    private String name;

    @Size(max = 500)
    private String description;
}
