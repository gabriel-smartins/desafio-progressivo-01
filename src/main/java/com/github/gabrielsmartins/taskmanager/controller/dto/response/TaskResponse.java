package com.github.gabrielsmartins.taskmanager.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.gabrielsmartins.taskmanager.model.Priority;
import com.github.gabrielsmartins.taskmanager.model.Status;
import com.github.gabrielsmartins.taskmanager.model.Task;

import java.sql.Timestamp;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TaskResponse(UUID id, String title,
                           String description, Status status,
                           Priority priority, Timestamp dueTime) {
    public TaskResponse(Task task) {
        this(task.getId(), task.getTitle(),
                task.getDescription(), task.getStatus(),
                task.getPriority(), task.getDueTime());
    }
}
