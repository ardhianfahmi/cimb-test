package com.ardhian.callmonitoring.auth.controller;

import com.ardhian.callmonitoring.auth.dto.response.UserListResponse;
import com.ardhian.callmonitoring.auth.service.UserService;
import com.ardhian.callmonitoring.common.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserListResponse>> getAllUsers() {
        UserListResponse data = userService.getAllUsers();

        String message = data.getItems().isEmpty()
                ? "No users found"
                : "Users fetched successfully";

        return ResponseEntity.ok(ApiResponse.success(message, data));
    }
}
