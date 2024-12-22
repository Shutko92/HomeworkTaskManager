package org.homework.taskmanagerservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class NewTaskRequest {
    private String title;
    private String description;
    @NotNull(message = "userId must not be empty")
    private Long userId;
}
