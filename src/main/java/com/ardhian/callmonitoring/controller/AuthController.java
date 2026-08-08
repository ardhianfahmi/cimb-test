package com.ardhian.callmonitoring.controller;

import com.ardhian.callmonitoring.dto.ApiResponse;
import com.ardhian.callmonitoring.dto.request.LoginRequest;
import com.ardhian.callmonitoring.dto.request.RegisterRequest;
import com.ardhian.callmonitoring.dto.response.LoginResponse;
import com.ardhian.callmonitoring.dto.response.RegisterResponse;
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
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        RegisterResponse data = authService.register(request);
        ApiResponse<RegisterResponse> response = ApiResponse.success(
                "Register success. Please login with your account.",
                data
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
