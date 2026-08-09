package com.ardhian.callmonitoring.callrecord.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "call_monitoring")
public class CallRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "call_id", nullable = false, unique = true)
    private String callId;

    @Column(name = "call_timestamp", nullable = false)
    private LocalDateTime callTimeStamp;

    @Column(name = "cs_name", nullable = false)
    private String csName;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @JdbcTypeCode(SqlTypes.SMALLINT)
    @Column(name = "sentiment_score", nullable = false)
    private Integer sentimentScore;

    public CallRecord() {
    }

    // GETTER
    public Long getId() {
        return id;
    }

    public String getCallId() {
        return callId;
    }

    public LocalDateTime getCallTimeStamp() {
        return callTimeStamp;
    }

    public String getCsName() {
        return csName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Integer getSentimentScore() {
        return sentimentScore;
    }

    // SETTER
    public void setId(Long id) {
        this.id = id;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public void setCallTimeStamp(LocalDateTime callTimeStamp) {
        this.callTimeStamp = callTimeStamp;
    }

    public void setCsName(String csName) {
        this.csName = csName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setSentimentScore(Integer sentimentScore) {
        this.sentimentScore = sentimentScore;
    }
}