package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should return all users")
    void shouldReturnAllUsers() {
        Users user1 = new Users();
        user1.setUsername("UserA");
        Users user2 = new Users();
        user2.setUsername("UserB");
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<Users> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return user by id")
    void shouldReturnUserById() {
        Users user = new Users();
        user.setUsername("UserA");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Users result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("UserA", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw 404 when user by id not found")
    void shouldThrowWhenUserByIdNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.getUserById(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("Should create user")
    void shouldCreateUser() {
        Users payload = new Users();
        payload.setUsername("CreatedUser");
        when(userRepository.save(payload)).thenReturn(payload);

        Users result = userService.createUser(payload);

        assertEquals("CreatedUser", result.getUsername());
        verify(userRepository, times(1)).save(payload);
    }

    @Test
    @DisplayName("Should create user when Swagger sends id 0")
    void shouldCreateUserWhenIdIsZero() {
        Users payload = new Users();
        payload.setId(0L);
        payload.setUsername("swagger-user");
        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.createUser(payload);

        assertNull(payload.getId());
        verify(userRepository, times(1)).save(payload);
    }

    @Test
    @DisplayName("Should update user")
    void shouldUpdateUser() {
        Users existing = new Users();
        existing.setUsername("OldName");
        Users payload = new Users();
        payload.setUsername("NewName");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        Users result = userService.updateUser(1L, payload);

        assertEquals("NewName", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existing);
    }

    @Test
    @DisplayName("Should throw 404 when updating missing user")
    void shouldThrowWhenUpdatingMissingUser() {
        Users payload = new Users();
        payload.setUsername("Any");
        when(userRepository.findById(50L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.updateUser(50L, payload));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("Should delete user by id")
    void shouldDeleteUserById() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw 404 when deleting missing user")
    void shouldThrowWhenDeletingMissingUser() {
        when(userRepository.existsById(77L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.deleteUser(77L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
