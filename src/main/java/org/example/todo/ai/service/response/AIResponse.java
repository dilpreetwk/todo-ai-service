package org.example.todo.ai.service.response;

import lombok.Data;

@Data
public class AIResponse {
    private Output output;

    @Data
    public static class Output {
        private String content;
    }
}
