package com.example.projectmanagerapp.service;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository ProjectRepository;

    public ProjectService(ProjectRepository ProjectRepository) {
        this.ProjectRepository = ProjectRepository;
    }

    public List<Project> getAllProjects() {
        return ProjectRepository.findAll();
    }


    public Project createProject(Project project) {
        return ProjectRepository.save(project);
    }
}
