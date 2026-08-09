package com.ardhian.callmonitoring.callrecord.repository;

import com.ardhian.callmonitoring.callrecord.entity.CallRecord;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class CallRecordSpecification {
    // SEARCHING
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

    public static Specification<CallRecord> periodBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start == null && end == null) {
                return cb.conjunction();
            }
            if (start != null && end != null) {
                return cb.between(root.get("callTimeStamp"), start, end);
            }
            if (start != null) {
                return cb.greaterThanOrEqualTo(root.get("callTimeStamp"), start);
            }
            return cb.lessThanOrEqualTo(root.get("callTimeStamp"), end);
        };
    }
    public static Specification<CallRecord> sentimentBelow(Integer threshold) {
        return (root, query, cb) -> cb.lessThan(root.get("sentimentScore"), threshold);
    }
    public static Specification<CallRecord> sentimentAtLeast(Integer threshold) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("sentimentScore"), threshold);
    }
}