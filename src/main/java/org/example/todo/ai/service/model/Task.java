package org.example.todo.ai.service.model;

public class Task {

    public enum Priority {
        LOW, MEDIUM, HIGH;
    }

    public enum Status {
        PENDING, COMPLETED;
    }

    public enum Category {
        PERSONAL, WORK
    }
}
