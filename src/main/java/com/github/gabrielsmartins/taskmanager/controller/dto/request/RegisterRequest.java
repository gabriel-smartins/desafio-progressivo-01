package com.github.gabrielsmartins.taskmanager.controller.dto.request;

import com.github.gabrielsmartins.taskmanager.model.UserRole;

public record RegisterRequest(String name, String email, String password, UserRole role) {
}
