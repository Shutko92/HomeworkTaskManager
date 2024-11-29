package org.homework.taskmanagerservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.homework.taskmanagerservice.dto.request.NewTaskRequest;
import org.homework.taskmanagerservice.dto.request.UpdateTaskRequest;
import org.homework.taskmanagerservice.dto.response.TaskResponse;
import org.homework.taskmanagerservice.entity.Task;
import org.homework.taskmanagerservice.exception.TaskNotFoundException;
import org.homework.taskmanagerservice.mapper.TaskMapper;
import org.homework.taskmanagerservice.reposirory.TaskRepository;
import org.homework.taskmanagerservice.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskResponse createTask(NewTaskRequest request) {

        return taskMapper.toTaskResponse(
                taskRepository.save(
                        taskMapper.toTask(request)));
    }

    @Override
    public String deleteTask(long taskId) {
        taskRepository.deleteById(taskId);
        return String.format("Task removed by ID: %d", taskId);
    }

    @Override
    public TaskResponse updateTask(UpdateTaskRequest request) {
        Task task = getTaskIfExists(request.getTaskId());

        if (request.getDescription()!=null) {
            task.setDescription(request.getDescription());
        }
        if (request.getTitle()!=null) {
            task.setTitle(request.getTitle());
        }
        if (request.getUserId() != null) {
            task.setUserId(request.getUserId());
        }

        return taskMapper.toTaskResponse(taskRepository.save(task));
    }

    @Override
    public TaskResponse getTask(long taskId) {

        return taskMapper.toTaskResponse(getTaskIfExists(taskId));
    }

    @Override
    public List<TaskResponse> getTasks() {

        return taskMapper.toTaskResponseList(taskRepository.findAll());
    }

    private Task getTaskIfExists(long taskId) {
        return taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
    }
}
