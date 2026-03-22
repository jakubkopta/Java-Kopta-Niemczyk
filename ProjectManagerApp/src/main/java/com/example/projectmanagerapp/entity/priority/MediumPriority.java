package com.example.projectmanagerapp.entity.priority;

import com.example.projectmanagerapp.entity.TaskType;

public class MediumPriority implements PriorityLevel {
    @Override
    public TaskType getPriority() {
        return TaskType.MEDIUM_PRIORITY;
    }
}
