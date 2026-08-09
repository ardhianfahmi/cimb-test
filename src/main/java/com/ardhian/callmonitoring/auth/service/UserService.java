package com.ardhian.callmonitoring.auth.service;

import com.ardhian.callmonitoring.auth.dto.response.UserListResponse;
import com.ardhian.callmonitoring.auth.dto.response.UserResponse;
import com.ardhian.callmonitoring.auth.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserListResponse getAllUsers() {
        List<UserResponse> items = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(UserResponse::fromEntity)
                .toList();

        return new UserListResponse(items);
    }
}
