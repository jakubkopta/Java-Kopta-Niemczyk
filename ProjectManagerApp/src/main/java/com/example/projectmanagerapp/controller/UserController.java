package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management operations")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    @Operation(summary = "List users", description = "Returns all users.")
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Creates a new user.")
    public Users createUser(@RequestBody Users user) {
        return userRepository.save(user);
    }
}