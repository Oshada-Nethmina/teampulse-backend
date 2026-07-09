package com.teampulse.backend.specification;

import com.teampulse.backend.entity.WeeklyReport;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeeklyReportSpecification {
    public static Specification<WeeklyReport> filter(
            Long userId,
            Long projectId,
            LocalDate from,
            LocalDate to
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                predicates.add(cb.equal(root.get("user").get("userId"), userId));
            }

            if (projectId != null) {
                predicates.add(cb.equal(root.get("project").get("id"), projectId));
            }

            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("weekStartDate"), from));
            }

            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("weekEndDate"), to));
            }

            query.orderBy(cb.desc(root.get("weekStartDate")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
