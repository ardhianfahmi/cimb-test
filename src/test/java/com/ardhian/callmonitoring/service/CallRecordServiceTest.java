package com.ardhian.callmonitoring.service;

import com.ardhian.callmonitoring.dto.response.CallRecordResponse;
import com.ardhian.callmonitoring.entity.CallRecord;
import com.ardhian.callmonitoring.repository.CallRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// simple unit test for CallRecordService (no need real database)
@ExtendWith(MockitoExtension.class)
class CallRecordServiceTest {

    @Mock
    private CallRecordRepository repository;

    @InjectMocks
    private CallRecordService service;

    @Test
    void getCallRecords_shouldReturnMappedData() {
        // arrange: fake 1 record from DB
        CallRecord record = new CallRecord();
        record.setCallId("CALL-1001");
        record.setCallTimestamp(LocalDateTime.of(2026, 8, 1, 10, 0));
        record.setCsName("Andi Pratama");
        record.setCustomerName("Budi Santoso");
        record.setSentimentScore(85);

        Page<CallRecord> fakePage = new PageImpl<>(List.of(record));
        when(repository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(fakePage);

        // act
        Page<CallRecordResponse> result = service.getCallRecords(
                null,
                null,
                null,
                null,
                PageRequest.of(0, 5)
        );

        // assert
        assertEquals(1, result.getTotalElements());
        assertEquals("CALL-1001", result.getContent().get(0).getCallId());
        assertEquals(85, result.getContent().get(0).getSentimentScore());
    }

    @Test
    void getCallRecords_shouldRejectStartDateOlderThan3Months() {
        LocalDateTime tooOld = LocalDateTime.now().minusMonths(4);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.getCallRecords(null, tooOld, null, null, PageRequest.of(0, 5))
        );

        assertTrue(ex.getReason().contains("3 months"));
    }
}
