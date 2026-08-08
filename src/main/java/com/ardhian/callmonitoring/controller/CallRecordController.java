package com.ardhian.callmonitoring.controller;

import com.ardhian.callmonitoring.dto.ApiResponse;
import com.ardhian.callmonitoring.dto.response.CallRecordResponse;
import com.ardhian.callmonitoring.service.CallRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/call-monitoring")
public class CallRecordController {

    private final CallRecordService service;

    public CallRecordController(CallRecordService service) {
        this.service = service;
    }

    // GET /api/call-monitoring?search=andi&startDate=...&endDate=...&sentimentFilter=below70&page=0&size=5&sort=callTimestamp,desc
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CallRecordResponse>>> getCallRecords(
            @RequestParam(required = false) String search,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate,
            @RequestParam(required = false) String sentimentFilter,
            @PageableDefault(size = 5, sort = "callTimestamp", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<CallRecordResponse> data = service.getCallRecords(
                search, startDate, endDate, sentimentFilter, pageable
        );

        String message;
        if (data.isEmpty()) {
            message = "No call records found";
        } else {
            message = "Call records fetched successfully";
        }

        ApiResponse<Page<CallRecordResponse>> response = ApiResponse.success(message, data);
        return ResponseEntity.ok(response);
    }
}
