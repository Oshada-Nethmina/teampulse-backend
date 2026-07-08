package com.teampulse.backend.service.impl;

import com.teampulse.backend.dto.response.UserResponse;
import com.teampulse.backend.entity.User;
import com.teampulse.backend.enums.Role;
import com.teampulse.backend.exception.ResourceNotFoundException;
import com.teampulse.backend.repository.UserRepository;
import com.teampulse.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserResponse> listTeamMembers() {
        return userRepository.findByRole(Role.TEAM_MEMBER).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<UserResponse> listAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public UserResponse findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getUserId(), user.getFullName(), user.getEmail(), user.getRole().name(), user.getActive());
    }
}
