package com.teampulse.backend.repository;

import com.teampulse.backend.entity.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long>, JpaSpecificationExecutor<WeeklyReport> {
    Optional<WeeklyReport> findByUser_UserIdAndWeekStartDate(
            Long userId,
            LocalDate weekStartDate);
    List<WeeklyReport> findByUser_UserIdOrderByWeekStartDateDesc(Long userId);

    List<WeeklyReport> findByWeekStartDate(LocalDate weekStartDate);
    List<WeeklyReport> findTop20ByOrderByUpdatedAtDesc();
    @Query("""
    SELECT r
    FROM WeeklyReport r
    WHERE r.weekStartDate = :weekStartDate
      AND r.blockers IS NOT NULL
      AND TRIM(r.blockers) <> ''
""")
    List<WeeklyReport> findOpenBlockersForWeek(
            @Param("weekStartDate") LocalDate weekStartDate
    );
}
