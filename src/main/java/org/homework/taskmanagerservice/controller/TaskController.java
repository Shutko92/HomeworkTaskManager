package org.homework.taskmanagerservice.controller;

import lombok.RequiredArgsConstructor;
import org.homework.taskmanagerservice.dto.request.NewTaskRequest;
import org.homework.taskmanagerservice.dto.request.UpdateTaskRequest;
import org.homework.taskmanagerservice.dto.response.TaskResponse;
import org.homework.taskmanagerservice.service.TaskService;
import org.springbootstarter.loggingstarter.annotation.ControllerLogger;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@ControllerLogger
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/task")
    public TaskResponse createTask(@RequestBody @Validated NewTaskRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping("/task/{taskId}")
    public TaskResponse getTaskById(@PathVariable Long taskId) {
        return taskService.getTask(taskId);
    }

    @PutMapping("/task")
    public TaskResponse updateTask(@RequestBody @Validated UpdateTaskRequest request) {
        return taskService.updateTask(request);
    }

    @GetMapping("/tasks")
    public List<TaskResponse> getAllTasks() {
        return taskService.getTasks();
    }

    @DeleteMapping("/task/{taskId}")
    public String deleteTask(@PathVariable Long taskId) {
        return taskService.deleteTask(taskId);
    }
}
