package com.ardhian.callmonitoring.callrecord.service;

import com.ardhian.callmonitoring.callrecord.dto.response.CallRecordPageResponse;
import com.ardhian.callmonitoring.callrecord.entity.CallRecord;
import com.ardhian.callmonitoring.callrecord.repository.CallRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CallRecordServiceTest {

    @Mock
    private CallRecordRepository repository;

    @InjectMocks
    private CallRecordService service;

    @Test
    void getCallRecords_mapsEntityToResponse() {
        CallRecord entity = new CallRecord();
        entity.setCallId("CALL-001");
        entity.setCallTimeStamp(LocalDateTime.of(2026, 1, 10, 10, 0));
        entity.setCsName("Budi");
        entity.setCustomerName("Andi");
        entity.setSentimentScore(80);

        when(repository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(entity)));

        CallRecordPageResponse result = service.getCallRecords(
                null, null, null, null, null, null, 0, 5);

        assertEquals(1, result.getItems().size());
        assertEquals("CALL-001", result.getItems().get(0).getCallId());
        assertEquals(80, result.getItems().get(0).getSentimentScore());
        assertEquals(0, result.getPage());
        assertEquals(1, result.getTotalItems());
        assertEquals(1, result.getTotalPages());
    }

    @Test
    void getCallRecords_defaultsSortAndPaging() {
        when(repository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(Page.empty());

        service.getCallRecords(null, null, null, null, "hacked", "nope", -1, 0);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findAll(any(Specification.class), pageableCaptor.capture());

        Pageable pageable = pageableCaptor.getValue();
        assertEquals(0, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());
        assertEquals("callTimeStamp", pageable.getSort().iterator().next().getProperty());
        assertTrue(pageable.getSort().getOrderFor("callTimeStamp").isDescending());
    }

    @Test
    void getCallRecords_startDateOlderThan3Months_throws() {
        LocalDateTime tooOld = LocalDateTime.now().minusMonths(4);

        assertThrows(ResponseStatusException.class, () ->
                service.getCallRecords(null, tooOld, null, null, null, null, 0, 5));

        verifyNoInteractions(repository);
    }

    @Test
    void getCallRecords_startAfterEnd_throws() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().minusDays(10);

        assertThrows(ResponseStatusException.class, () ->
                service.getCallRecords(null, start, end, null, null, null, 0, 5));

        verifyNoInteractions(repository);
    }
}