package com.github.gabrielsmartins.taskmanager.controller.dto.request;

import com.github.gabrielsmartins.taskmanager.model.UserRole;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        String name,

        String email,

        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
        String password,

        UserRole role) {
}
