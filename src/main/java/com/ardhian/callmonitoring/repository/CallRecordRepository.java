package com.ardhian.callmonitoring.repository;

import com.ardhian.callmonitoring.entity.CallRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

// ALLOW DYNAMIC FILTER (SEARCH, DATE, SENTIMENT)
public interface CallRecordRepository extends JpaRepository<CallRecord, Long>,
        JpaSpecificationExecutor<CallRecord> {
}
