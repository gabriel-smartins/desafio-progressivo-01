package com.github.gabrielsmartins.taskmanager.controller;

import com.github.gabrielsmartins.taskmanager.controller.dto.request.TaskRequest;
import com.github.gabrielsmartins.taskmanager.controller.dto.request.TaskUpdateRequest;
import com.github.gabrielsmartins.taskmanager.controller.dto.response.TaskResponse;
import com.github.gabrielsmartins.taskmanager.model.Priority;
import com.github.gabrielsmartins.taskmanager.model.Status;
import com.github.gabrielsmartins.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> save(@RequestBody @Valid TaskRequest request) {
        var taskToSave = request.toDomain();
        var savedTask = taskService.save(taskToSave, request.categoryId());
        var response = new TaskResponse(savedTask);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> findAllByFilters(@RequestParam(required = false) Status status,
                                                               @RequestParam(required = false) Priority priority,
                                                               @PageableDefault(size = 20,
                                                                       sort = "createdAt",
                                                                       direction = Sort.Direction.DESC) Pageable pageable) {
        var tasks = taskService.findAllByFilter(status, priority, pageable);

        var response = tasks.map(TaskResponse::new);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable UUID id) {
        var task = taskService.findById(id);

        var response = new TaskResponse(task);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody TaskUpdateRequest request) {
        var payload = request.toDomain();
        taskService.update(id, payload, request.categoryId());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> completeTask(@PathVariable UUID id) {
        taskService.completeTask(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taskService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
