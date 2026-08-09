package com.ardhian.callmonitoring.callrecord.controller;

import com.ardhian.callmonitoring.callrecord.dto.response.CallRecordResponse;
import com.ardhian.callmonitoring.callrecord.service.CallRecordService;
import com.ardhian.callmonitoring.common.api.ApiResponse;
import org.springframework.data.domain.Page;
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

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CallRecordResponse>>> getCallRecords(
        // search=
            @RequestParam(required = false) String search,

            // start_date=
            @RequestParam(value = "start_date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,

            // end_date=
            @RequestParam(value = "end_date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate,

            // sentiment=
            @RequestParam(required = false) String sentiment,

            // sort_by=
            @RequestParam(value = "sort_by", required = false, defaultValue = "callTimeStamp")
            String sortBy,

            // sort_order=
            @RequestParam(value = "sort_order", required = false, defaultValue = "desc")
            String sortOrder,

            // page=
            @RequestParam(required = false, defaultValue = "0") int page,
            // size=
            @RequestParam(required = false, defaultValue = "5") int size
    ) {
        Page<CallRecordResponse> data = service.getCallRecords(
                search, startDate, endDate, sentiment, sortBy, sortOrder, page, size
        );

        String message = data.isEmpty()
                ? "No call records found"
                : "Call records fetched successfully";

        return ResponseEntity.ok(ApiResponse.success(message, data));
    }
}