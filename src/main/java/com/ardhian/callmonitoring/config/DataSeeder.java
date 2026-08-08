package com.ardhian.callmonitoring.config;

import com.ardhian.callmonitoring.auth.entity.User;
import com.ardhian.callmonitoring.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// runs once when app starts (like a seed script after server.listen)
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // DEFAULT USER
        if (!userRepository.existsByUsername("supervisor")) {
            User supervisor = new User();
            supervisor.setUsername("supervisor");
            supervisor.setPassword(passwordEncoder.encode("password123"));
            supervisor.setFullName("Supervisor CIMB");
            supervisor.setEmail("supervisor@cimb.local");
            supervisor.setPhoneNumber("081234567890");
            supervisor.setRole("SUPERVISOR");
            userRepository.save(supervisor);
            System.out.println("Default supervisor user created!");
        }
    }
}
