package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        userRepository = mock(UserRepository.class);
        projectService = new ProjectService(projectRepository, userRepository);
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

    @Test
    @DisplayName("Should assign user to project")
    void shouldAssignUserToProject() {
        Project project = new Project();
        project.setName("Project");
        Users user = new Users();
        user.setUsername("member");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.assignUserToProject(1L, 2L);

        assertNotNull(result.getUsers());
        assertTrue(result.getUsers().contains(user));
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should append user to existing members")
    void shouldAppendUserToExistingMembers() {
        Users existingMember = new Users();
        existingMember.setUsername("existing");
        Users newMember = new Users();
        newMember.setUsername("new");

        Project project = new Project();
        project.setUsers(new java.util.HashSet<>(Set.of(existingMember)));

        when(projectRepository.findById(3L)).thenReturn(Optional.of(project));
        when(userRepository.findById(4L)).thenReturn(Optional.of(newMember));
        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.assignUserToProject(3L, 4L);

        assertEquals(2, result.getUsers().size());
        assertTrue(result.getUsers().contains(existingMember));
        assertTrue(result.getUsers().contains(newMember));
    }

    @Test
    @DisplayName("Should throw 404 when assigning user to missing project")
    void shouldThrowWhenAssigningUserToMissingProject() {
        when(projectRepository.findById(100L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> projectService.assignUserToProject(100L, 1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("Should throw 404 when assigning missing user to project")
    void shouldThrowWhenAssigningMissingUserToProject() {
        Project project = new Project();
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(200L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> projectService.assignUserToProject(1L, 200L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
