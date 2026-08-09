package com.ardhian.callmonitoring.callrecord.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;

import java.util.List;

public class CallRecordPageResponse {

    private List<CallRecordResponse> items;
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;

    public CallRecordPageResponse() {
    }

    public static CallRecordPageResponse from(Page<CallRecordResponse> page) {
        CallRecordPageResponse response = new CallRecordPageResponse();
        response.setItems(page.getContent());
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalItems(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        return response;
    }

    public List<CallRecordResponse> getItems() {
        return items;
    }

    public void setItems(List<CallRecordResponse> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @JsonProperty("total_items")
    public long getTotalItems() {
        return totalItems;
    }

    @JsonProperty("total_items")
    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    @JsonProperty("total_pages")
    public int getTotalPages() {
        return totalPages;
    }

    @JsonProperty("total_pages")
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
