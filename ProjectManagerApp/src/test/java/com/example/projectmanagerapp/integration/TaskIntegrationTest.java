package com.example.projectmanagerapp.integration;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldPerformTaskCrudFlow() throws Exception {
        String createUserPayload = """
                {
                  "username": "task-owner"
                }
                """;

        String createProjectPayload = """
                {
                  "name": "Task Project"
                }
                """;

        String createTaskPayload = """
                {
                  "title": "Initial task",
                  "description": "Initial description",
                  "taskType": "MEDIUM_PRIORITY",
                  "project": { "id": 1 },
                  "user": { "id": 1 }
                }
                """;

        String updateTaskPayload = """
                {
                  "title": "Updated task",
                  "description": "Updated description",
                  "taskType": "HIGH_PRIORITY",
                  "project": { "id": 1 },
                  "user": { "id": 1 }
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(createUserPayload))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/projects")
                        .contentType("application/json")
                        .content(createProjectPayload))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/tasks")
                        .contentType("application/json")
                        .content(createTaskPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Initial task"))
                .andExpect(jsonPath("$.taskType").value("MEDIUM_PRIORITY"))
                .andExpect(jsonPath("$.project.id").value(1))
                .andExpect(jsonPath("$.user.id").value(1));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Initial task"));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].title", hasItem("Initial task")));

        mockMvc.perform(put("/api/tasks/1")
                        .contentType("application/json")
                        .content(updateTaskPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated task"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.taskType").value("HIGH_PRIORITY"));

        mockMvc.perform(put("/api/tasks/999")
                        .contentType("application/json")
                        .content(updateTaskPayload))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/tasks/999"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isNotFound());
    }
}
