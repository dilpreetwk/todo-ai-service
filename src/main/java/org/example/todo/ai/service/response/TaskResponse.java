package org.example.todo.ai.service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.todo.ai.service.model.Task;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TaskResponse {
    private UUID id;
    private String title;
    private String description;
    private Task.Priority priority;
    private Task.Status status;
    private Task.Category category;
}
