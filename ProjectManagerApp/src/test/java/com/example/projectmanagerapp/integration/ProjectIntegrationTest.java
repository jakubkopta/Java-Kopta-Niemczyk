package com.example.projectmanagerapp.integration;

import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldPerformProjectCrudFlow() throws Exception {
        String createPayload = """
                {
                  "name": "Integration Project"
                }
                """;

        String updatePayload = """
                {
                  "name": "Updated Integration Project"
                }
                """;

        mockMvc.perform(post("/api/projects")
                        .contentType("application/json")
                        .content(createPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Integration Project"));

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Integration Project"));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItem("Integration Project")));

        mockMvc.perform(put("/api/projects/1")
                        .contentType("application/json")
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Integration Project"));

        mockMvc.perform(put("/api/projects/999")
                        .contentType("application/json")
                        .content(updatePayload))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/projects/999"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isNotFound());
    }
}