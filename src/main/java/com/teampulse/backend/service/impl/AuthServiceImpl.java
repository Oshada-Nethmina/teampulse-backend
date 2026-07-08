package com.teampulse.backend.service.impl;

import com.teampulse.backend.dto.request.LoginRequest;
import com.teampulse.backend.dto.request.RegisterRequest;
import com.teampulse.backend.dto.response.AuthResponse;
import com.teampulse.backend.entity.User;
import com.teampulse.backend.enums.Role;
import com.teampulse.backend.exception.BadRequestException;
import com.teampulse.backend.repository.UserRepository;
import com.teampulse.backend.security.JwtUtils;
import com.teampulse.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("An account with this email already exists");
        }

        // Self-registration always defaults to TEAM_MEMBER unless explicitly requested.
        // Promoting someone to MANAGER should go through an admin-only endpoint in a real deployment.
        Role role = registerRequest.getRole() == null ? Role.TEAM_MEMBER : registerRequest.getRole();

        User user = User.builder()
                .fullName(registerRequest.getFullName())
                .email(registerRequest.getEmail().toLowerCase())
                .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .active(true)
                .build();

        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name(), user.getUserId());
        return new AuthResponse(token, user.getUserId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail().toLowerCase(), loginRequest.getPassword())
        );

        User user = userRepository.findByEmail(loginRequest.getEmail().toLowerCase())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name(), user.getUserId());
        return new AuthResponse(token, user.getUserId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }
}
