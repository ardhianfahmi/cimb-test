package com.ardhian.callmonitoring.callrecord.controller;

import com.ardhian.callmonitoring.callrecord.dto.response.CallRecordPageResponse;
import com.ardhian.callmonitoring.callrecord.service.CallRecordService;
import com.ardhian.callmonitoring.common.api.ApiResponse;
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
    public ResponseEntity<ApiResponse<CallRecordPageResponse>> getCallRecords(
            @RequestParam(required = false) String search,

            @RequestParam(value = "start_date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,

            @RequestParam(value = "end_date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate,

            @RequestParam(required = false) String sentiment,

            @RequestParam(value = "sort_by", required = false, defaultValue = "call_timestamp")
            String sortBy,

            @RequestParam(value = "sort_order", required = false, defaultValue = "desc")
            String sortOrder,

            @RequestParam(required = false, defaultValue = "0") int page,

            @RequestParam(required = false, defaultValue = "5") int size
    ) {
        CallRecordPageResponse data = service.getCallRecords(
                search, startDate, endDate, sentiment, sortBy, sortOrder, page, size
        );

        String message = data.getItems().isEmpty()
                ? "No call records found"
                : "Call records fetched successfully";

        return ResponseEntity.ok(ApiResponse.success(message, data));
    }
}
