package com.example.projectmanagerapp.entity.priority;

import com.example.projectmanagerapp.entity.TaskType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriorityLevelTest {

    @Test
    void shouldReturnHighPriority() {
        PriorityLevel priority = new HighPriority();
        assertEquals(TaskType.HIGH_PRIORITY, priority.getPriority());
    }

    @Test
    void shouldReturnMediumPriority() {
        PriorityLevel priority = new MediumPriority();
        assertEquals(TaskType.MEDIUM_PRIORITY, priority.getPriority());
    }

    @Test
    void shouldReturnLowPriority() {
        PriorityLevel priority = new LowPriority();
        assertEquals(TaskType.LOW_PRIORITY, priority.getPriority());
    }
}
