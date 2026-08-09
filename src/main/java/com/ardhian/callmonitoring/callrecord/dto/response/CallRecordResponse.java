package com.ardhian.callmonitoring.callrecord.dto.response;

import com.ardhian.callmonitoring.callrecord.entity.CallRecord;

import java.time.LocalDateTime;

public class CallRecordResponse {

    private String callId;
    private LocalDateTime callTimestamp;
    private String csName;
    private String customerName;
    private Integer sentimentScore;

    public CallRecordResponse() {
    }

    public static CallRecordResponse fromEntity(CallRecord entity) {
        CallRecordResponse response = new CallRecordResponse();
        response.setCallId(entity.getCallId());
        response.setCallTimestamp(entity.getCallTimeStamp());
        response.setCsName(entity.getCsName());
        response.setCustomerName(entity.getCustomerName());
        response.setSentimentScore(entity.getSentimentScore());
        return response;
    }

    public String getCallId() { return callId; }
    public void setCallId(String callId) { this.callId = callId; }

    public LocalDateTime getCallTimestamp() { return callTimestamp; }
    public void setCallTimestamp(LocalDateTime callTimestamp) { this.callTimestamp = callTimestamp; }

    public String getCsName() { return csName; }
    public void setCsName(String csName) { this.csName = csName; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public Integer getSentimentScore() { return sentimentScore; }
    public void setSentimentScore(Integer sentimentScore) { this.sentimentScore = sentimentScore; }
}