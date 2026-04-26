package com.example.projectmanagerapp.integration;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectUserRelationIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldAssignUserToProjectAndExposeMemberInProjectDetails() throws Exception {
        String createUserPayload = """
                {
                  "username": "relation-user"
                }
                """;
        String createProjectPayload = """
                {
                  "name": "Relation Project"
                }
                """;
        String createSecondUserPayload = """
                {
                  "username": "relation-user-2"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(createUserPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(post("/api/projects")
                        .contentType("application/json")
                        .content(createProjectPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(post("/api/projects/1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.users[*].id", hasItem(1)))
                .andExpect(jsonPath("$.users[*].username", hasItem("relation-user")));

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[*].id", hasItem(1)))
                .andExpect(jsonPath("$.users[*].username", hasItem("relation-user")));

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(createSecondUserPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));

        mockMvc.perform(post("/api/projects/1/users/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[*].id", hasItem(1)))
                .andExpect(jsonPath("$.users[*].id", hasItem(2)));

        mockMvc.perform(post("/api/projects/999/users/1"))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/projects/1/users/999"))
                .andExpect(status().isNotFound());
    }
}
