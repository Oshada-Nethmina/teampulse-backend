package com.teampulse.backend.repository;

import com.teampulse.backend.entity.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long> {
    Optional<WeeklyReport> findByUser_UserIdAndWeekStartDate(
            Long userId,
            LocalDate weekStartDate);
    List<WeeklyReport> findByUser_UserIdOrderByWeekStartDateDesc(Long userId);
    @Query("""
    SELECT r
    FROM WeeklyReport r
    WHERE (:userId IS NULL OR r.user.userId = :userId)
      AND (:projectId IS NULL OR r.project.id = :projectId)
      AND (:from IS NULL OR r.weekStartDate >= :from)
      AND (:to IS NULL OR r.weekEndDate <= :to)
    ORDER BY r.weekStartDate DESC
""")
    List<WeeklyReport> search(
            @Param("userId") Long userId,
            @Param("projectId") Long projectId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
