package com.ardhian.callmonitoring.callrecord.repository;

import com.ardhian.callmonitoring.callrecord.entity.CallRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CallRecordSpecificationTest {

    @Autowired
    private CallRecordRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        repository.save(record("CALL-001", "Budi", "Andi", 80,
                LocalDateTime.of(2026, 1, 10, 10, 0)));
        repository.save(record("CALL-002", "Siti", "Rina", 40,
                LocalDateTime.of(2026, 1, 15, 10, 0)));
        repository.save(record("CALL-003", "Budi", "Tono", 20,
                LocalDateTime.of(2026, 2, 1, 10, 0)));
    }

    @Test
    void search_byCustomerName() {
        List<CallRecord> result = repository.findAll(
                CallRecordSpecification.search("rina"));

        assertEquals(1, result.size());
        assertEquals("CALL-002", result.get(0).getCallId());
    }

    @Test
    void search_blank_returnsAll() {
        List<CallRecord> result = repository.findAll(
                CallRecordSpecification.search("  "));

        assertEquals(3, result.size());
    }

    @Test
    void periodBetween_filtersByTimestamp() {
        List<CallRecord> result = repository.findAll(
                CallRecordSpecification.periodBetween(
                        LocalDateTime.of(2026, 1, 1, 0, 0),
                        LocalDateTime.of(2026, 1, 31, 23, 59)));

        assertEquals(2, result.size());
    }

    @Test
    void sentimentBelow_filtersLowScores() {
        List<CallRecord> result = repository.findAll(
                CallRecordSpecification.sentimentBelow(50));

        assertEquals(2, result.size());
    }

    @Test
    void combined_specs() {
        List<CallRecord> result = repository.findAll(
                CallRecordSpecification.search("budi")
                        .and(CallRecordSpecification.sentimentAtLeast(50)));

        assertEquals(1, result.size());
        assertEquals("CALL-001", result.get(0).getCallId());
    }

    private CallRecord record(String callId, String cs, String customer,
                              int score, LocalDateTime ts) {
        CallRecord r = new CallRecord();
        r.setCallId(callId);
        r.setCsName(cs);
        r.setCustomerName(customer);
        r.setSentimentScore(score);
        r.setCallTimeStamp(ts);
        return r;
    }
}