package org.homework.taskmanagerservice.service;

import org.homework.taskmanagerservice.dto.request.NewTaskRequest;
import org.homework.taskmanagerservice.dto.request.UpdateTaskRequest;
import org.homework.taskmanagerservice.dto.response.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(NewTaskRequest request);
    String deleteTask(long taskId);
    TaskResponse updateTask(UpdateTaskRequest request);
    TaskResponse getTask(long taskId);
    List<TaskResponse> getTasks();
}
