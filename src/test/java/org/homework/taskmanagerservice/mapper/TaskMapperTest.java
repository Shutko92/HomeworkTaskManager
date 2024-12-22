package org.homework.taskmanagerservice.mapper;

import org.homework.taskmanagerservice.dto.request.NewTaskRequest;
import org.homework.taskmanagerservice.dto.response.TaskResponse;
import org.homework.taskmanagerservice.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.homework.taskmanagerservice.entity.TaskStatus.NEW;
import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {
    private TaskMapper mapper;
    private NewTaskRequest testNewTaskRequest;
    private Task testTask;

    @BeforeEach
    void setUp() {
        mapper = new TaskMapperImpl();
        testNewTaskRequest = getTestNewTaskRequest();
        testTask = getTestTask();
    }

    @Test
    void toTask() {
        Task result = mapper.toTask(testNewTaskRequest);
        assertNotNull(result);
        assertEquals(testTask.getTitle(), result.getTitle());
        assertEquals(testTask.getDescription(), result.getDescription());
        assertEquals(testTask.getUserId(), result.getUserId());
    }

    @Test
    void toTaskResponse() {
        TaskResponse result = mapper.toTaskResponse(testTask);
        assertEquals(testTask.getTitle(), result.getTitle());
        assertEquals(testTask.getDescription(), result.getDescription());
        assertEquals(testTask.getUserId(), result.getUserId());
        assertEquals(testTask.getStatus(), result.getStatus());
    }

    @Test
    void toTaskResponseList() {
        List<TaskResponse> result = mapper.toTaskResponseList(List.of(testTask));
        assertEquals(testTask.getTitle(), result.get(0).getTitle());
        assertEquals(testTask.getDescription(), result.get(0).getDescription());
        assertEquals(testTask.getUserId(), result.get(0).getUserId());
        assertEquals(testTask.getStatus(), result.get(0).getStatus());
    }

    private NewTaskRequest getTestNewTaskRequest() {
        return NewTaskRequest.builder()
                .title("test title")
                .description("test description")
                .userId(1L).build();
    }

    private Task getTestTask() {
        return Task.builder()
                .title("test title")
                .description("test description")
                .userId(1L)
                .status(NEW).build();
    }
}