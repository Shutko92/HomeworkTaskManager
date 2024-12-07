package org.homework.taskmanagerservice.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.homework.taskmanagerservice.entity.TaskStatus;

@Getter
@Setter
@ToString
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private long userId;
    private TaskStatus status;
}
