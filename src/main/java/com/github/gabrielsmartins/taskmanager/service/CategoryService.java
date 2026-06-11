package com.github.gabrielsmartins.taskmanager.service;

import com.github.gabrielsmartins.taskmanager.model.Category;
import com.github.gabrielsmartins.taskmanager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    @Transactional
    public Category save(Category category) {
        return repository.save(category);
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return repository.findAll();
    }

}
