package com.github.gabrielsmartins.taskmanager.controller.dto.request;

import com.github.gabrielsmartins.taskmanager.model.Priority;
import com.github.gabrielsmartins.taskmanager.model.Task;

import java.sql.Timestamp;
import java.util.UUID;

public record TaskUpdateRequest(String title,
                                String description,
                                Priority priority,
                                Timestamp dueTime,
                                UUID categoryId){
    public Task toDomain() {
        Task domain = new Task();
        domain.setTitle(this.title());
        domain.setDescription(this.description());
        domain.setPriority(this.priority());
        domain.setDueTime(this.dueTime());

        return domain;
    }

}
