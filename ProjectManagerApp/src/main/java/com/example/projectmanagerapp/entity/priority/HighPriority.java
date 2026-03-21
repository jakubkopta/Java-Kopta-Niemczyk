package com.example.projectmanagerapp.entity.priority;

import com.example.projectmanagerapp.entity.TaskType;

public class HighPriority implements PriorityLevel {
    @Override
    public TaskType getPriority() {
        return TaskType.HIGH_PRIORITY;
    }
}
