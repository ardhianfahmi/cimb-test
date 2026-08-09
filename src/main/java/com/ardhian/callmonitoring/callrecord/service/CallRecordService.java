package com.ardhian.callmonitoring.callrecord.service;

import com.ardhian.callmonitoring.callrecord.dto.response.CallRecordResponse;
import com.ardhian.callmonitoring.callrecord.entity.CallRecord;
import com.ardhian.callmonitoring.callrecord.repository.CallRecordRepository;
import com.ardhian.callmonitoring.callrecord.repository.CallRecordSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class CallRecordService {

    private static final Set<String> ALLOWED_SORT =
            Set.of("callTimestamp", "sentimentScore", "csName", "customerName", "callId");

    private final CallRecordRepository repository;

    public CallRecordService(CallRecordRepository repository) {
        this.repository = repository;
    }

    public Page<CallRecordResponse> getCallRecords(
            String search,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String sentiment,
            String sortBy,
            String sortOrder,
            int page,
            int size
    ) {
        validatePeriodLimit(startDate, endDate);

        Specification<CallRecord> spec = Specification
                .where(CallRecordSpecification.search(search))
                .and(CallRecordSpecification.periodBetween(startDate, endDate));

        if (sentiment != null) {
            if ("below70".equalsIgnoreCase(sentiment)) {
                spec = spec.and(CallRecordSpecification.sentimentBelow(70));
            } else if ("above70".equalsIgnoreCase(sentiment)) {
                spec = spec.and(CallRecordSpecification.sentimentAtLeast(70));
            }
        }

        String safeSortBy = (sortBy != null && ALLOWED_SORT.contains(sortBy))
                ? sortBy
                : "callTimestamp";

        Sort.Direction direction = "asc".equalsIgnoreCase(sortOrder)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        int safePage = Math.max(page, 0);
        int safeSize = size > 0 ? size : 5;

        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(direction, safeSortBy));

        return repository.findAll(spec, pageable)
                .map(CallRecordResponse::fromEntity);
    }

    private void validatePeriodLimit(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);

        if (startDate != null && startDate.isBefore(threeMonthsAgo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date cannot be older than 3 months");
        }
        if (endDate != null && endDate.isBefore(threeMonthsAgo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be older than 3 months");
        }
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date cannot be after end date");
        }
    }
}