package com.teampulse.backend.repository;

import com.teampulse.backend.entity.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @EntityGraph(attributePaths = "members")
    List<Project> findByActiveTrue();
    boolean existsByNameIgnoreCase(String name);
    @EntityGraph(attributePaths = "members")
    List<Project> findAll();
    @EntityGraph(attributePaths = "members")
    Optional<Project> findById(Long id);

}
