package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
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

class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    @DisplayName("Should return all projects")
    void shouldReturnAllProjects() {
        Project project1 = new Project();
        project1.setName("P1");
        Project project2 = new Project();
        project2.setName("P2");
        when(projectRepository.findAll()).thenReturn(List.of(project1, project2));

        List<Project> result = projectService.getAllProjects();

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return project by id")
    void shouldReturnProjectById() {
        Project project = new Project();
        project.setName("MyProject");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById(1L);

        assertNotNull(result);
        assertEquals("MyProject", result.getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw 404 when project by id not found")
    void shouldThrowWhenProjectByIdNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> projectService.getProjectById(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("Should create project")
    void shouldCreateProject() {
        Project payload = new Project();
        payload.setName("CreatedProject");
        when(projectRepository.save(payload)).thenReturn(payload);

        Project result = projectService.createProject(payload);

        assertEquals("CreatedProject", result.getName());
        verify(projectRepository, times(1)).save(payload);
    }

    @Test
    @DisplayName("Should update project")
    void shouldUpdateProject() {
        Project existing = new Project();
        existing.setName("OldProject");
        Project payload = new Project();
        payload.setName("NewProject");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(projectRepository.save(existing)).thenReturn(existing);

        Project result = projectService.updateProject(1L, payload);

        assertEquals("NewProject", result.getName());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(existing);
    }

    @Test
    @DisplayName("Should throw 404 when updating missing project")
    void shouldThrowWhenUpdatingMissingProject() {
        Project payload = new Project();
        payload.setName("Any");
        when(projectRepository.findById(50L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> projectService.updateProject(50L, payload));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("Should delete project by id")
    void shouldDeleteProjectById() {
        when(projectRepository.existsById(1L)).thenReturn(true);

        projectService.deleteProject(1L);

        verify(projectRepository, times(1)).existsById(1L);
        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw 404 when deleting missing project")
    void shouldThrowWhenDeletingMissingProject() {
        when(projectRepository.existsById(77L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> projectService.deleteProject(77L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
