package com.ardhian.callmonitoring.callrecord.dto.response;

import com.ardhian.callmonitoring.callrecord.entity.CallRecord;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class CallRecordResponse {

    private String callId;
    private LocalDateTime callTimeStamp;
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

    @JsonProperty("call_id")
    public String getCallId() {
        return callId;
    }

    @JsonProperty("call_id")
    public void setCallId(String callId) {
        this.callId = callId;
    }

    @JsonProperty("call_timestamp")
    public LocalDateTime getCallTimestamp() {
        return callTimeStamp;
    }

    @JsonProperty("call_timestamp")
    public void setCallTimestamp(LocalDateTime callTimeStamp) {
        this.callTimeStamp = callTimeStamp;
    }

    @JsonProperty("cs_name")
    public String getCsName() {
        return csName;
    }

    @JsonProperty("cs_name")
    public void setCsName(String csName) {
        this.csName = csName;
    }

    @JsonProperty("customer_name")
    public String getCustomerName() {
        return customerName;
    }

    @JsonProperty("customer_name")
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @JsonProperty("sentiment_score")
    public Integer getSentimentScore() {
        return sentimentScore;
    }

    @JsonProperty("sentiment_score")
    public void setSentimentScore(Integer sentimentScore) {
        this.sentimentScore = sentimentScore;
    }
}
