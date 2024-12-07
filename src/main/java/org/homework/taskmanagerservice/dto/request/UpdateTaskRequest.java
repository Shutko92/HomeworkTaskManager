package org.homework.taskmanagerservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.homework.taskmanagerservice.entity.TaskStatus;

@ToString
@Getter
public class UpdateTaskRequest {
    private String title;
    private String description;
    private Long userId;
    @NotNull(message = "taskId must not be empty")
    private Long taskId;
    private TaskStatus status;
}
