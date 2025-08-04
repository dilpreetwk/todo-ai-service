package org.example.todo.ai.service.service;

import lombok.RequiredArgsConstructor;
import org.example.todo.ai.service.response.AIResponse;
import org.example.todo.ai.service.response.TaskResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AIService {

    private final TaskClientService taskClientService;
    private final LLMClientService llmClientService;

    public AIResponse generateTaskSummary(UUID userId) {
        List<TaskResponse> dueToday = taskClientService.getDueToday(userId);
        List<TaskResponse> overDue = taskClientService.getOverDue(userId);

        StringBuilder prompt = new StringBuilder("""
                You are an AI productivity assistant. Based on the user's task list, analyze and respond strictly in this structure:
                Important: Do NOT invent or assume any tasks. Only refer to the tasks listed below. Do not add, omit, or alter any task data.

                
                1. Overall Summary:
                - Total tasks due today: <number>
                - Total overdue tasks: <number>
                - Quick Recap: "You have a <light/busy> day ahead with <X> tasks due today and <Y> overdue."
                
                2. Task Breakdown:
                
                Tasks Due Today (sorted by priority High → Low, then by category):
                """);
        sortAndAppendToPromptByCategory(dueToday, prompt);

        prompt.append("\nOverdue Tasks (highlight HIGH priority first):\n");
        sortAndAppendToPromptByCategory(overDue, prompt);

        prompt.append("""
                
                3. Action Recommendations:
                - Suggested next steps or focus areas (high-priority tasks to tackle first)
                - Smart suggestions: What can be rescheduled or delegated?
                
                4. Time-Based Suggestions:
                - What tasks are due soonest today?
                - Any gaps in the day to fit more work?
                
                5. Motivational Nudge:
                - Provide a short, relevant motivational quote or encouragement.
                
                Now, generate the response.
                """);

        return llmClientService.executeQuery(prompt.toString());
    }

    private void sortAndAppendToPromptByCategory(List<TaskResponse> dueToday, StringBuilder prompt) {
        dueToday.stream()
                .sorted((a, b) -> {
                    int p = b.getPriority().compareTo(a.getPriority());
                    return p != 0 ? p : a.getCategory().compareTo(b.getCategory());
                })
                .forEach(task -> prompt.append("- [")
                        .append(task.getPriority()).append(" | ")
                        .append(task.getCategory()).append("] ")
                        .append(task.getTitle()).append(": ").append(task.getDescription()).append("\n"));
    }
}
