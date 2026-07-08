package com.teampulse.backend.service.impl;

import com.teampulse.backend.dto.request.ProjectRequest;
import com.teampulse.backend.dto.response.ProjectResponse;
import com.teampulse.backend.entity.Project;
import com.teampulse.backend.exception.BadRequestException;
import com.teampulse.backend.exception.ResourceNotFoundException;
import com.teampulse.backend.repository.ProjectRepository;
import com.teampulse.backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    @Override
    public List<ProjectResponse> listActiveProjects() {
        return projectRepository.findByActiveTrue().stream().map(this::toResponse).toList();
    }

    @Override
    public List<ProjectResponse> listAllProjects() {
        return projectRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    @Override
    public ProjectResponse createProject(ProjectRequest projectRequest) {
        if (projectRepository.existsByNameIgnoreCase(projectRequest.getName())) {
            throw new BadRequestException("A project with this name already exists");
        }
        Project project = Project.builder()
                .name(projectRequest.getName())
                .description(projectRequest.getDescription())
                .active(true)
                .build();
        return toResponse(projectRepository.save(project));
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest projectRequest) {
        Project project = getProjectById(id);

        String newName = projectRequest.getName().trim();

        if (!project.getName().equalsIgnoreCase(newName)
                && projectRepository.existsByNameIgnoreCase(newName)) {
            throw new BadRequestException("A project with this name already exists");
        }

        project.setName(newName);
        project.setDescription(projectRequest.getDescription());

        return toResponse(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        Project project = getProjectById(id);

        // Soft delete keeps historical reports referencing this project intact.
        project.setActive(false);
        projectRepository.save(project);
    }

    @Override
    public ProjectResponse findProject(Long id) {
        Project project = getProjectById(id);

        return toResponse(project);
    }

    private Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + id));
    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(project.getId(), project.getName(), project.getDescription(), project.getActive());
    }
}
