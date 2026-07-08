package com.teampulse.backend.service;

import com.teampulse.backend.dto.request.LoginRequest;
import com.teampulse.backend.dto.request.RegisterRequest;
import com.teampulse.backend.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
