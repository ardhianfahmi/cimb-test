package com.ardhian.callmonitoring.auth.dto.response;

import java.util.List;

public class UserListResponse {

    private List<UserResponse> items;

    public UserListResponse() {
    }

    public UserListResponse(List<UserResponse> items) {
        this.items = items;
    }

    public List<UserResponse> getItems() {
        return items;
    }

    public void setItems(List<UserResponse> items) {
        this.items = items;
    }
}
