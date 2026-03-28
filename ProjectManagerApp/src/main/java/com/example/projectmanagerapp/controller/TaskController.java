package com.example.projectmanagerapp.controller;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Task management operations")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "List tasks", description = "Returns all tasks.")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    @Operation(summary = "Create task", description = "Creates a new task.")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }
}