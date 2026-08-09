package com.ardhian.callmonitoring.callrecord.repository;

import com.ardhian.callmonitoring.callrecord.entity.CallRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CallRecordRepository
        extends JpaRepository<CallRecord, Long>, JpaSpecificationExecutor<CallRecord> {
}