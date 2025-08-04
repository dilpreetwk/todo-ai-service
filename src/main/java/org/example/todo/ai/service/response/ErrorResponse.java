package org.example.todo.ai.service.response;

import lombok.Builder;
import org.example.todo.ai.service.exception.ApiErrorDetails;

import java.util.List;

@Builder
public record ErrorResponse(
        String code,
        String title,
        List<ApiErrorDetails> errors
) {
}