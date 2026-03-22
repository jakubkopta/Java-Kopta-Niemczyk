package com.example.projectmanagerapp.entity.priority;

import com.example.projectmanagerapp.entity.TaskType;

public class LowPriority implements PriorityLevel {
    @Override
    public TaskType getPriority() {
        return TaskType.LOW_PRIORITY;
    }
}
