package com.github.gabrielsmartins.taskmanager.service;

import com.github.gabrielsmartins.taskmanager.exception.BusinessRuleException;
import com.github.gabrielsmartins.taskmanager.exception.ResourceNotFoundException;
import com.github.gabrielsmartins.taskmanager.model.*;
import com.github.gabrielsmartins.taskmanager.repository.CategoryRepository;
import com.github.gabrielsmartins.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Task save(Task task, UUID categoryId, User user) {
        task.setCategory(resolveCategory(categoryId));
        task.setCreator(user);

        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public Page<Task> findAllByFilter(Status status, Priority priority, Pageable pageable, User user) {
        if (user.getRole() == UserRole.ADMIN) {
            return taskRepository.findAllByFilters(status, priority, pageable);
        }
        return taskRepository.findAllByFiltersAndCreatorId(status, priority, user.getId(), pageable);
    }

    @Transactional(readOnly = true)
    public Task findById(UUID id, User user) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada."));

        boolean isOwner = task.getCreator().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == UserRole.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Você não tem permissão para acessar este recurso.");
        }

        return task;
    }

    @Transactional
    public void update(UUID id, Task payload, UUID categoryId, User user) {
        var entity = this.findById(id, user);

        if (entity.getStatus() == Status.DONE) {
            throw new BusinessRuleException("Não é possível editar uma tarefa que já está concluída.");
        }

        entity.setTitle(payload.getTitle());
        entity.setDescription(payload.getDescription());
        entity.setCategory(resolveCategory(categoryId));
        entity.setPriority(payload.getPriority());
        entity.setDueTime(payload.getDueTime());
    }

    @Transactional
    public void completeTask(UUID id, User user) {
        var entity = this.findById(id, user);

        entity.setStatus(Status.DONE);
    }

    @Transactional
    public void delete(UUID id, User user) {
        var entity = this.findById(id, user);
        taskRepository.delete(entity);
    }

    private Category resolveCategory(UUID categoryId) {
        if (categoryId == null) return null;
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada."));
    }
}
