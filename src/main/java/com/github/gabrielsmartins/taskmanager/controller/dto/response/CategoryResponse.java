package com.github.gabrielsmartins.taskmanager.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.gabrielsmartins.taskmanager.model.Category;

import java.util.UUID;

public record CategoryResponse(UUID id, String name) {
    public CategoryResponse(Category category) {
        this(category.getId(), category.getName());
    }
}
