package com.teampulse.backend.service;

import com.teampulse.backend.dto.request.ProjectRequest;
import com.teampulse.backend.dto.response.ProjectResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectResponse> listActiveProjects();
    List<ProjectResponse> listAllProjects();
    ProjectResponse createProject(ProjectRequest projectRequest);
    ProjectResponse updateProject(Long id, ProjectRequest projectRequest);
    void deleteProject(Long id);
    ProjectResponse findProject(Long id);
}
