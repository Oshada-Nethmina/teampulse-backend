package com.teampulse.backend.service;

import com.teampulse.backend.dto.response.UserResponse;
import com.teampulse.backend.entity.User;

import java.util.List;

public interface UserService {
    List<UserResponse> listTeamMembers();
    List<UserResponse> listAll();
    UserResponse  findUserById(Long userId);
}
