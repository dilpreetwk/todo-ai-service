package org.example.todo.ai.service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.todo.ai.service.response.AIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class LLMClientService {

    @Value("${llm.secrets.api-key}")
    private String apiKey;

    @Value("${llm.secrets.user-id}")
    private String userId;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String url = "https://jg2a3ht375wlztvg5hve3lamae0tjlml.lambda-url.us-east-1.on.aws/agent/todo-agent/execute";

    public AIResponse executeQuery(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-user-id", userId);
        headers.set("x-authentication", "api-key " + apiKey);

        Map<String, Object> body = Map.of(
                "input", Map.of("query", query)
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate
                .exchange(url, HttpMethod.POST, request, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getBody(), AIResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }
}