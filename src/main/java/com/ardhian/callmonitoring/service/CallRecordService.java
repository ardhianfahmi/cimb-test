package com.ardhian.callmonitoring.service;

import com.ardhian.callmonitoring.dto.CallRecordResponse;
import com.ardhian.callmonitoring.entity.CallRecord;
import com.ardhian.callmonitoring.repository.CallRecordRepository;
import com.ardhian.callmonitoring.repository.CallRecordSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class CallRecordService {

    private final CallRecordRepository repository;

    public CallRecordService(CallRecordRepository repository) {
        this.repository = repository;
    }

    public Page<CallRecordResponse> getCallRecords(
            String search,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String sentimentFilter,
            Pageable pageable
    ) {
        // user story: period can only be latest 3 months
        validatePeriodLimit(startDate, endDate);

        // build filter step by step
        Specification<CallRecord> spec = Specification
                .where(CallRecordSpecification.search(search))
                .and(CallRecordSpecification.periodBetween(startDate, endDate));

        // sentiment filter values from frontend:
        // below70  = Di bawah 70%
        // above70  = 70% atau lebih
        if (sentimentFilter != null) {
            if ("below70".equalsIgnoreCase(sentimentFilter)) {
                spec = spec.and(CallRecordSpecification.sentimentBelow(70));
            } else if ("above70".equalsIgnoreCase(sentimentFilter)) {
                spec = spec.and(CallRecordSpecification.sentimentAtLeast(70));
            }
        }

        // user story: 5 records per page
        // if frontend not send size, we force size = 5
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize() > 0 ? pageable.getPageSize() : 5;
        Sort sort = pageable.getSort().isSorted()
                ? pageable.getSort()
                : Sort.by(Sort.Direction.DESC, "callTimestamp");

        Pageable finalPageable = PageRequest.of(page, size, sort);

        return repository.findAll(spec, finalPageable)
                .map(CallRecordResponse::fromEntity);
    }

    // check selected period is not older than 3 months
    private void validatePeriodLimit(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);

        if (startDate != null && startDate.isBefore(threeMonthsAgo)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Start date cannot be older than 3 months"
            );
        }

        if (endDate != null && endDate.isBefore(threeMonthsAgo)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End date cannot be older than 3 months"
            );
        }

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Start date cannot be after end date"
            );
        }
    }
}
