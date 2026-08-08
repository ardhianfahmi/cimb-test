package com.ardhian.callmonitoring.controller;

import com.ardhian.callmonitoring.dto.ApiResponse;
import com.ardhian.callmonitoring.dto.LoginRequest;
import com.ardhian.callmonitoring.dto.LoginResponse;
import com.ardhian.callmonitoring.dto.RegisterRequest;
import com.ardhian.callmonitoring.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        ApiResponse<Object> response = ApiResponse.success(
                "Register success. Please login with your account.",
                null
        );
        return ResponseEntity.ok(response);
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse data = authService.login(request);
        ApiResponse<LoginResponse> response = ApiResponse.success("Login success", data);
        return ResponseEntity.ok(response);
    }
}
