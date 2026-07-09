package com.teampulse.backend.controller;

import com.teampulse.backend.dto.request.ChatRequest;
import com.teampulse.backend.dto.response.ChatResponse;
import com.teampulse.backend.service.AiChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiChatController {
    private final AiChatService aiChatService;

    @PostMapping("/chat")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ChatResponse> chat(
            @Valid @RequestBody ChatRequest request) {

        String answer = aiChatService.chat(request.getMessage());

        return ResponseEntity.ok(
                new ChatResponse(answer)
        );
    }
}
