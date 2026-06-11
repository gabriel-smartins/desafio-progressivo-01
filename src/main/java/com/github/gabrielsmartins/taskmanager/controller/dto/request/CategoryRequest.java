package com.github.gabrielsmartins.taskmanager.controller.dto.request;

import com.github.gabrielsmartins.taskmanager.model.Category;

public record CategoryRequest(String name) {

    public Category toDomain() {
        Category domain = new Category();
        domain.setName(this.name());

        return domain;
    }
}
