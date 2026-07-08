package com.teampulse.backend.repository;

import com.teampulse.backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByActiveTrue();
    boolean existsByNameIgnoreCase(String name);
}
