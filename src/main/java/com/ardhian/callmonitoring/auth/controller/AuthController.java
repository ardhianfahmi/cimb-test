package com.ardhian.callmonitoring.auth.controller;

import com.ardhian.callmonitoring.auth.dto.request.LoginRequest;
import com.ardhian.callmonitoring.auth.dto.request.RegisterRequest;
import com.ardhian.callmonitoring.auth.dto.response.LoginResponse;
import com.ardhian.callmonitoring.auth.dto.response.RegisterResponse;
import com.ardhian.callmonitoring.auth.service.AuthService;
import com.ardhian.callmonitoring.common.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Like Express: router.post("/api/auth/...", handler)
// @RestController = this class returns JSON
// @RequestMapping = base path for all routes below
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Spring injects AuthService (like NestJS constructor DI)
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        // @RequestBody = JSON body -> Java object (like req.body)
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
