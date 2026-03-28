package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Project management operations")
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    @Operation(summary = "List projects", description = "Returns all projects.")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Create project", description = "Creates a new project.")
    public Project createProject(@RequestBody Project project) {
        project.setId(null);
        return projectRepository.save(project);
    }
}
