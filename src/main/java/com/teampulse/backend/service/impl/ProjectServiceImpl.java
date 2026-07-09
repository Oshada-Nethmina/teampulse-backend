package com.teampulse.backend.service.impl;

import com.teampulse.backend.dto.request.ProjectRequest;
import com.teampulse.backend.dto.response.ProjectResponse;
import com.teampulse.backend.entity.Project;
import com.teampulse.backend.entity.User;
import com.teampulse.backend.exception.BadRequestException;
import com.teampulse.backend.exception.ResourceNotFoundException;
import com.teampulse.backend.repository.ProjectRepository;
import com.teampulse.backend.repository.UserRepository;
import com.teampulse.backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> listActiveProjects() {
        return projectRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> listAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
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
                .active(projectRequest.getActive())
                .build();

        if (projectRequest.getAssignedMemberIds() != null &&
                !projectRequest.getAssignedMemberIds().isEmpty()) {

            List<User> members =
                    userRepository.findAllById(projectRequest.getAssignedMemberIds());

            project.setMembers(new HashSet<>(members));
        }

        Project savedProject = projectRepository.save(project);

        return toResponse(savedProject);
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

        project.setName(projectRequest.getName());
        project.setDescription(projectRequest.getDescription());
        project.setActive(projectRequest.getActive());

            project.setMembers(new HashSet<>());

            if (projectRequest.getAssignedMemberIds() != null) {

                List<User> members =
                        userRepository.findAllById(projectRequest.getAssignedMemberIds());

                project.setMembers(new HashSet<>(members));
            }
        Project updatedProject = projectRepository.save(project);

        return toResponse(updatedProject);
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
    @Transactional(readOnly = true)
    public ProjectResponse findProject(Long id) {
        Project project = getProjectById(id);
        return toResponse(project);
    }

    private Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + id));
    }

    private ProjectResponse toResponse(Project project) {

        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .active(project.getActive())
                .assignedMemberIds(
                        project.getMembers()
                                .stream()
                                .map(User::getUserId)
                                .toList()
                )
                .build();
    }
}
