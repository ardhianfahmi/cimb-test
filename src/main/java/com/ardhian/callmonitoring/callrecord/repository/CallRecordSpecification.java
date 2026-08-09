package com.ardhian.callmonitoring.callrecord.repository;

import com.ardhian.callmonitoring.callrecord.entity.CallRecord;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CallRecordSpecification {

    public static Specification<CallRecord> search(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }

            String[] tokens = keyword.trim().toLowerCase().split("\\s+");
            List<Predicate> tokenPredicates = new ArrayList<>();

            for (String token : tokens) {
                String pattern = "%" + token + "%";
                List<Predicate> fieldPredicates = new ArrayList<>();
                fieldPredicates.add(cb.like(cb.lower(root.get("callId")), pattern));
                fieldPredicates.add(cb.like(cb.lower(root.get("csName")), pattern));
                fieldPredicates.add(cb.like(cb.lower(root.get("customerName")), pattern));

                // sentiment_score is smallint — never use lower()/like on it
                if (token.matches("\\d+")) {
                    fieldPredicates.add(cb.equal(root.get("sentimentScore"), Integer.valueOf(token)));
                }

                tokenPredicates.add(cb.or(fieldPredicates.toArray(Predicate[]::new)));
            }

            // any token may match (OR) — e.g. search=call+hehe
            return cb.or(tokenPredicates.toArray(Predicate[]::new));
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
