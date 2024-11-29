package org.homework.taskmanagerservice.dto.request;

import lombok.Getter;

@Getter
public class NewTaskRequest {
    private String title;
    private String description;
    private long userId;
}
