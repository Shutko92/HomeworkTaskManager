package org.homework.taskmanagerservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NewTaskRequest {
    private String title;
    private String description;
    @NotNull(message = "userId must not be empty")
    private Long userId;
}