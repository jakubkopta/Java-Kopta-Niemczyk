package com.example.projectmanagerapp.service;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository TaskRepository;

    public TaskService(TaskRepository TaskRepository) {
        this.TaskRepository = TaskRepository;
    }

    public List<Task> getAllTasks() {
        return TaskRepository.findAll();
    }


    public Task createTask(Task task) {
        return TaskRepository.save(task);
    }
}
