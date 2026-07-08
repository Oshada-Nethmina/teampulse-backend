package com.teampulse.backend.controller;


import com.teampulse.backend.dto.response.UserResponse;
import com.teampulse.backend.entity.User;
import com.teampulse.backend.security.UserPrincipal;
import com.teampulse.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get-user")
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(userService.findUserById(principal.getId()));
    }


    @GetMapping("/getAll")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.listAll());
    }

    @GetMapping("/team-members")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<UserResponse>> getTeamMembers() {
        return ResponseEntity.ok(userService.listTeamMembers());
    }
}
