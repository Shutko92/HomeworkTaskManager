package org.homework.taskmanagerservice.dto.request;

import lombok.Getter;

@Getter
public class UpdateTaskRequest {
    private String title;
    private String description;
    private Long userId;
    private Long taskId;
}
