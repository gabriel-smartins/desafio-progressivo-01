package com.github.gabrielsmartins.taskmanager.controller.dto.request;

import com.github.gabrielsmartins.taskmanager.model.Category;
import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "O nome da categoria é obrigatório")
        String name) {

    public Category toDomain() {
        Category domain = new Category();
        domain.setName(this.name());

        return domain;
    }
}
