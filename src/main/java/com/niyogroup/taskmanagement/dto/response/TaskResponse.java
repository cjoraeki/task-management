package com.niyogroup.taskmanagement.dto.response;

import com.niyogroup.taskmanagement.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponse {
    private String title;

    private String description;

    private TaskStatus taskStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;
}
