package com.example.projectmanagerapp.controller;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import com.example.projectmanagerapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management operations")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "List users", description = "Returns all users.")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Creates a new user.")
    public ResponseEntity<Users> createUser(@RequestBody Users user)
    {
        Users createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}