package org.example.todo.ai.service.controller;

import lombok.RequiredArgsConstructor;
import org.example.todo.ai.service.response.AIResponse;
import org.example.todo.ai.service.service.AIService;
import org.example.todo.ai.service.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AIController {

    private final JwtService jwtService;
    private final AIService aiService;

    @PostMapping("/summary")
    public ResponseEntity<AIResponse> summary(
            @RequestHeader("Authorization") String authHeader
    ) {
        UUID userId = extractUserIdFromAuthHeader(authHeader);

        return ResponseEntity.ok(aiService.generateTaskSummary(userId));
    }

    private UUID extractUserIdFromAuthHeader(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return UUID.fromString(jwtService.extractUserId(token));
    }

}
