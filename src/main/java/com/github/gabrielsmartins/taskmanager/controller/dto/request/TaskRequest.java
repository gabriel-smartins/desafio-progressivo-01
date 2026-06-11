package com.github.gabrielsmartins.taskmanager.controller.dto.request;

import com.github.gabrielsmartins.taskmanager.model.Priority;
import com.github.gabrielsmartins.taskmanager.model.Task;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.UUID;

public record TaskRequest(
        @NotBlank(message = "Título deve ter entre 3 e 120 caracteres.")
        String title,

        String description,

        @NotNull(message = "Defina uma prioridade -> 'LOW', 'MEDIUM' or 'HIGH'.")
        Priority priority,

        @FutureOrPresent(message = "A data de entrega não pode estar no passado.")
        Timestamp dueTime,

        UUID categoryId
) {
    public Task toDomain() {
        Task domain = new Task();
        domain.setTitle(this.title());
        domain.setDescription(this.description());
        domain.setPriority(this.priority());
        domain.setDueTime(this.dueTime());

        return domain;
    }
}
