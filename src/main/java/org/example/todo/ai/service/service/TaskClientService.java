package org.example.todo.ai.service.service;

import org.example.todo.ai.service.response.TaskResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class TaskClientService {
    @Value("${api-gateway.url}")
    private String apiGateway;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<TaskResponse> getDueToday(UUID userId) {
        String url = apiGateway + "/api/v1/task/admin/getDueToday/" + userId;
        ResponseEntity<TaskResponse[]> response = restTemplate.getForEntity(
                url,
                TaskResponse[].class
        );

        if (response.getBody() == null) {
            return List.of();
        }

        return Arrays.asList(response.getBody());
    }

    public List<TaskResponse> getOverDue(UUID userId) {
        String url = apiGateway + "/api/v1/task/admin/getOverDue/" + userId;
        ResponseEntity<TaskResponse[]> response = restTemplate.getForEntity(
                url,
                TaskResponse[].class
        );

        if (response.getBody() == null) {
            return List.of();
        }

        return Arrays.asList(response.getBody());
    }
}

