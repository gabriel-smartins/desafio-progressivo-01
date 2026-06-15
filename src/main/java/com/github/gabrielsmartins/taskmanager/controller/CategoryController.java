package com.github.gabrielsmartins.taskmanager.controller;

import com.github.gabrielsmartins.taskmanager.controller.dto.request.CategoryRequest;
import com.github.gabrielsmartins.taskmanager.controller.dto.response.CategoryResponse;
import com.github.gabrielsmartins.taskmanager.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> save(@RequestBody @Valid CategoryRequest request) {
        var savedCategory = categoryService.save(request.toDomain());
        var response = new CategoryResponse(savedCategory);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        var categories = categoryService.findAll()
                .stream()
                .map(CategoryResponse::new)
                .toList();

        return ResponseEntity.ok(categories);
    }
}
