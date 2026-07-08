package com.teampulse.backend.controller;

import com.teampulse.backend.dto.request.ProjectRequest;
import com.teampulse.backend.dto.response.ProjectResponse;
import com.teampulse.backend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    // Any authenticated user can view active projects.
    @GetMapping("/activeList")
    public ResponseEntity<List<ProjectResponse>> listActiveProjects() {
        return ResponseEntity.ok(projectService.listActiveProjects());
    }

    // Only managers can view all projects (including inactive ones).
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<ProjectResponse>> listAllProjects() {
        return ResponseEntity.ok(projectService.listAllProjects());
    }

    // Get a project by ID.
    @GetMapping("find/{id}")
    public ResponseEntity<ProjectResponse> findProject(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findProject(id));
    }

    // Only managers can create a project.
    @PostMapping("/create-project")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody ProjectRequest projectRequest) {

        return ResponseEntity.ok(projectService.createProject(projectRequest));
    }

    // Only managers can update a project.
    @PutMapping("/update-project/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest projectRequest) {

        return ResponseEntity.ok(projectService.updateProject(id, projectRequest));
    }

    // Only managers can delete (soft delete) a project.
    @DeleteMapping("/delete-project/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {

        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
