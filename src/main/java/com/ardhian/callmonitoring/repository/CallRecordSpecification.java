package com.ardhian.callmonitoring.repository;

import com.ardhian.callmonitoring.entity.CallRecord;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

// Specification = dynamic WHERE condition for JPA
public class CallRecordSpecification {

    // search in Call ID, CS Name, Customer Name, Sentiment Score
    public static Specification<CallRecord> search(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("callId")), pattern),
                    cb.like(cb.lower(root.get("csName")), pattern),
                    cb.like(cb.lower(root.get("customerName")), pattern),
                    cb.like(cb.lower(root.get("sentimentScore").as(String.class)), pattern)
            );
        };
    }

    // FILTER BY START / END DATE
    public static Specification<CallRecord> periodBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start == null && end == null) {
                return cb.conjunction();
            }

            if (start != null && end != null) {
                return cb.between(root.get("callTimestamp"), start, end);
            }

            if (start != null) {
                return cb.greaterThanOrEqualTo(root.get("callTimestamp"), start);
            }

            return cb.lessThanOrEqualTo(root.get("callTimestamp"), end);
        };
    }

    // <70
    public static Specification<CallRecord> sentimentBelow(Integer threshold) {
        return (root, query, cb) -> cb.lessThan(root.get("sentimentScore"), threshold);
    }

    // >=70
    public static Specification<CallRecord> sentimentAtLeast(Integer threshold) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("sentimentScore"), threshold);
    }
}
