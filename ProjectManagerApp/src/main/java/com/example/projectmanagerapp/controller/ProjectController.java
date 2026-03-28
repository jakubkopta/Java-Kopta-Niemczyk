package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Project management operations")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation(summary = "List projects", description = "Returns all projects.")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    @Operation(summary = "Create project", description = "Creates a new project.")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project createdProject = projectService.createProject(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project", description = "Updates an existing project by id.")
    public Project updateProject(@PathVariable Long id, @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project", description = "Deletes a project by id.")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }
}
