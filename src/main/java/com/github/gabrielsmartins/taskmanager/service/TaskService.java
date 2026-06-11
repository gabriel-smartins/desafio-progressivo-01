package com.github.gabrielsmartins.taskmanager.service;

import com.github.gabrielsmartins.taskmanager.exception.BusinessRuleException;
import com.github.gabrielsmartins.taskmanager.exception.ResourceNotFoundException;
import com.github.gabrielsmartins.taskmanager.model.Priority;
import com.github.gabrielsmartins.taskmanager.model.Status;
import com.github.gabrielsmartins.taskmanager.model.Task;
import com.github.gabrielsmartins.taskmanager.repository.CategoryRepository;
import com.github.gabrielsmartins.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Task save(Task task, UUID categoryId) {
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada."));

        task.setCategory(category);

        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public Page<Task> findAllByFilter(Status status, Priority priority, Pageable pageable) {
        return taskRepository.findAllByFilters(status, priority, pageable);
    }

    @Transactional(readOnly = true)
    public Task findById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada."));
    }

    @Transactional
    public void update(UUID id, Task payload, UUID categoryId) {
        var entity = this.findById(id);

        if(entity.getStatus() == Status.DONE) {
            throw new BusinessRuleException("Não é possível editar uma tarefa que já está concluída.");
        }

        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada."));

        entity.setTitle(payload.getTitle());
        entity.setDescription(payload.getDescription());
        entity.setCategory(category);
    }

    @Transactional
    public void completeTask(UUID id) {
        var entity = this.findById(id);

        entity.setStatus(Status.DONE);
    }

    @Transactional
    public void delete(UUID id) {
        var entity = this.findById(id);
        taskRepository.delete(entity);
    }


}
