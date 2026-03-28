package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Task management operations")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    @Operation(summary = "List tasks", description = "Returns all tasks.")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Create task", description = "Creates a new task.")
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }
}