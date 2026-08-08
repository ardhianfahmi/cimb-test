package com.ardhian.callmonitoring.auth.service;

import com.ardhian.callmonitoring.auth.dto.request.LoginRequest;
import com.ardhian.callmonitoring.auth.dto.request.RegisterRequest;
import com.ardhian.callmonitoring.auth.dto.response.LoginResponse;
import com.ardhian.callmonitoring.auth.dto.response.RegisterResponse;
import com.ardhian.callmonitoring.auth.entity.User;
import com.ardhian.callmonitoring.auth.repository.UserRepository;
import com.ardhian.callmonitoring.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

// @Service = business logic layer (like a service class in NestJS / Express)
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // register only creates account, user must login after this
    public RegisterResponse register(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        }
        if (request.getFullName() == null || request.getFullName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is required");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is required");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        // new User() + setters = like const user = { username, password, ... }
        User user = new User();
        user.setUsername(request.getUsername().trim());
        // HASH PASSWORD (bcrypt) — never store plain password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPhoneNumber(request.getPhoneNumber().trim());
        user.setRole("SUPERVISOR");

        User saved = userRepository.save(user);

        return new RegisterResponse(
                saved.getUsername(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getPhoneNumber(),
                saved.getRole()
        );
    }

    public LoginResponse login(LoginRequest request) {
        // Optional = value may be empty (like user | undefined)
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        User user = userOptional.get();

        boolean passwordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!passwordMatch) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        return toLoginResponse(user);
    }

    private LoginResponse toLoginResponse(User user) {
        String token = jwtUtil.generateToken(user.getUsername());
        return new LoginResponse(
                token,
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole()
        );
    }
}
