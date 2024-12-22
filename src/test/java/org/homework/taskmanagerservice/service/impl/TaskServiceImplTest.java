package org.homework.taskmanagerservice.service.impl;

import org.homework.taskmanagerservice.dto.request.NewTaskRequest;
import org.homework.taskmanagerservice.dto.request.UpdateTaskRequest;
import org.homework.taskmanagerservice.dto.response.TaskResponse;
import org.homework.taskmanagerservice.entity.Task;
import org.homework.taskmanagerservice.exception.TaskNotFoundException;
import org.homework.taskmanagerservice.reposirory.TaskRepository;
import org.homework.taskmanagerservice.testcontainer.BaseTestContainerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.homework.taskmanagerservice.entity.TaskStatus.NEW;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TaskServiceImplTest extends BaseTestContainerTest {
    @Autowired
    private TaskServiceImpl taskService;
    @Autowired
    private TaskRepository taskRepository;

    private Task testTask;
    private TaskResponse testTaskResponse;
    private NewTaskRequest testNewTaskRequest;
    private UpdateTaskRequest testUpdateTaskRequest;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        testTask = getTestTask();
        testTaskResponse = getTestTaskResponse();
        testNewTaskRequest = getTestNewTaskRequest();
        testUpdateTaskRequest = getTestUpdateTaskRequest();
    }

    @Test
    void createTaskReturnsDto() {
        TaskResponse response = taskService.createTask(testNewTaskRequest);
        assertNotNullAndEquals(testTaskResponse, response);
    }


    @Test
    void deleteTaskReturnsMessage() {
        Task saved = taskRepository.save(testTask);
        String response = taskService.deleteTask(saved.getId());
        assertEquals("Task removed by ID: " + saved.getId(), response);
        /*
        Я несколько раз слышал, что методы на удаление должны быть идемпотентными.
        Поэтому метод всегда возвращает сообщение об удалении.
         */
    }

    @Test
    void updateTaskReturnsUpdatedDto() {
        Task saved = taskRepository.save(testTask);
        testUpdateTaskRequest.setTaskId(saved.getId());
        TaskResponse response = taskService.updateTask(testUpdateTaskRequest);
        assertEquals("new title", response.getTitle());
        assertEquals("new description", response.getDescription());
    }

    @Test
    void updateTaskThrowsException() {
        testUpdateTaskRequest.setTaskId(5L);
        assertThrows(TaskNotFoundException.class, ()->taskService.updateTask(testUpdateTaskRequest));
    }

    @Test
    void getTaskReturnsDto() {
        Task saved = taskRepository.save(testTask);
        TaskResponse response = taskService.getTask(saved.getId());
        assertNotNullAndEquals(testTaskResponse, response);
    }

    @Test
    void getTaskThrowsException() {
        assertThrows(TaskNotFoundException.class, ()->taskService.getTask(5L));
    }

    @Test
    void getTasksReturnsDtoList() {
        taskRepository.save(testTask);
        List<TaskResponse> response = taskService.getTasks();
        assertNotNullAndEquals(testTaskResponse, response.get(0));
    }

    private Task getTestTask() {
        return Task.builder()
                .title("test title")
                .description("test description")
                .userId(1L)
                .status(NEW).build();
    }

    private TaskResponse getTestTaskResponse() {
        return TaskResponse.builder()
                .id(1L)
                .title("test title")
                .description("test description")
                .userId(1L)
                .status(NEW).build();
    }

    private NewTaskRequest getTestNewTaskRequest() {
        return NewTaskRequest.builder()
                .title("test title")
                .description("test description")
                .userId(1L).build();
    }

    private UpdateTaskRequest getTestUpdateTaskRequest() {
        return UpdateTaskRequest.builder()
                .title("new title")
                .description("new description")
                .taskId(1L).build();
    }

    private void assertNotNullAndEquals(TaskResponse expected, TaskResponse actual) {
        assertNotNull(actual);
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getStatus(), actual.getStatus());
    }
}