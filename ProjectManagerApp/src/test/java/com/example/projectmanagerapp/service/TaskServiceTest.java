package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.TaskType;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    void shouldReturnAllTasks() {
        Task task1 = new Task();
        task1.setTitle("T1");
        Task task2 = new Task();
        task2.setTitle("T2");
        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by id")
    void shouldReturnTaskById() {
        Task task = new Task();
        task.setTitle("MyTask");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("MyTask", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw 404 when task by id not found")
    void shouldThrowWhenTaskByIdNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> taskService.getTaskById(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("Should create task")
    void shouldCreateTask() {
        Task payload = new Task();
        payload.setTitle("CreatedTask");
        when(taskRepository.save(payload)).thenReturn(payload);

        Task result = taskService.createTask(payload);

        assertEquals("CreatedTask", result.getTitle());
        verify(taskRepository, times(1)).save(payload);
    }

    @Test
    @DisplayName("Should update task")
    void shouldUpdateTask() {
        Project project = new Project();
        project.setName("ProjectA");
        Users user = new Users();
        user.setUsername("UserA");

        Task existing = new Task();
        existing.setTitle("OldTitle");
        existing.setDescription("OldDesc");
        existing.setTaskType(TaskType.LOW_PRIORITY);

        Task payload = new Task();
        payload.setTitle("NewTitle");
        payload.setDescription("NewDesc");
        payload.setTaskType(TaskType.HIGH_PRIORITY);
        payload.setProject(project);
        payload.setUser(user);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(existing);

        Task result = taskService.updateTask(1L, payload);

        assertEquals("NewTitle", result.getTitle());
        assertEquals("NewDesc", result.getDescription());
        assertEquals(TaskType.HIGH_PRIORITY, result.getTaskType());
        assertEquals(project, result.getProject());
        assertEquals(user, result.getUser());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existing);
    }

    @Test
    @DisplayName("Should throw 404 when updating missing task")
    void shouldThrowWhenUpdatingMissingTask() {
        Task payload = new Task();
        payload.setTitle("Any");
        when(taskRepository.findById(50L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> taskService.updateTask(50L, payload));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("Should delete task by id")
    void shouldDeleteTaskById() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw 404 when deleting missing task")
    void shouldThrowWhenDeletingMissingTask() {
        when(taskRepository.existsById(77L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> taskService.deleteTask(77L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
