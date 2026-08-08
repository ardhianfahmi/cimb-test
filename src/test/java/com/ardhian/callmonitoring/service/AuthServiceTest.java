package com.ardhian.callmonitoring.service;

import com.ardhian.callmonitoring.dto.LoginRequest;
import com.ardhian.callmonitoring.dto.LoginResponse;
import com.ardhian.callmonitoring.entity.User;
import com.ardhian.callmonitoring.repository.UserRepository;
import com.ardhian.callmonitoring.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_success_shouldReturnToken() {
        User user = new User();
        user.setUsername("supervisor");
        user.setPassword("hashed-password");
        user.setFullName("Supervisor CIMB");
        user.setEmail("supervisor@cimb.local");
        user.setPhoneNumber("081234567890");
        user.setRole("SUPERVISOR");

        LoginRequest request = new LoginRequest();
        request.setUsername("supervisor");
        request.setPassword("password123");

        when(userRepository.findByUsername("supervisor")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashed-password")).thenReturn(true);
        when(jwtUtil.generateToken("supervisor")).thenReturn("fake-jwt-token");

        LoginResponse response = authService.login(request);

        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("supervisor", response.getUsername());
        assertEquals("supervisor@cimb.local", response.getEmail());
        assertEquals("081234567890", response.getPhoneNumber());
        assertEquals("SUPERVISOR", response.getRole());
    }

    @Test
    void login_wrongPassword_shouldFail() {
        User user = new User();
        user.setUsername("supervisor");
        user.setPassword("hashed-password");

        LoginRequest request = new LoginRequest();
        request.setUsername("supervisor");
        request.setPassword("wrong");

        when(userRepository.findByUsername("supervisor")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed-password")).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> authService.login(request));
    }
}
