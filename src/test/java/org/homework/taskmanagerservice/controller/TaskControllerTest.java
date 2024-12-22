package org.homework.taskmanagerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.homework.taskmanagerservice.dto.request.NewTaskRequest;
import org.homework.taskmanagerservice.dto.request.UpdateTaskRequest;
import org.homework.taskmanagerservice.dto.response.TaskResponse;
import org.homework.taskmanagerservice.entity.Task;
import org.homework.taskmanagerservice.reposirory.TaskRepository;
import org.homework.taskmanagerservice.testcontainer.BaseTestContainerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.homework.taskmanagerservice.entity.TaskStatus.NEW;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TaskControllerTest extends BaseTestContainerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TaskRepository taskRepository;

    private Task testTask;
    private TaskResponse testTaskResponse;
    private NewTaskRequest testNewTaskRequest;
    private UpdateTaskRequest testUpdateTaskRequest;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        testTaskResponse = getTestTaskResponse();
        testTask = getTestTask();
        testNewTaskRequest = getTestNewTaskRequest();
        testUpdateTaskRequest = getTestUpdateTaskRequest();
    }

    @Test
    void createTaskReturnsDto() {
        try {
            mvc.perform(post("/api/task").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testNewTaskRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.title").value(testTaskResponse.getTitle()))
                    .andExpect(jsonPath("$.description").value(testTaskResponse.getDescription()))
                    .andExpect(jsonPath("$.userId").value(testTaskResponse.getUserId()))
                    .andExpect(jsonPath("$.status").value(testTaskResponse.getStatus().toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getTaskByIdReturnsDto() {
        Long id = taskRepository.save(testTask).getId();
        try {
            mvc.perform(get("/api/task/{taskId}", id))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.title").value(testTaskResponse.getTitle()))
                    .andExpect(jsonPath("$.description").value(testTaskResponse.getDescription()))
                    .andExpect(jsonPath("$.userId").value(testTaskResponse.getUserId()))
                    .andExpect(jsonPath("$.status").value(testTaskResponse.getStatus().toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getTaskByIdThrowsException() {
        try {
            mvc.perform(get("/api/task/{taskId}", 6))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateTaskReturnsUpdatedDto() {
        Long id = taskRepository.save(testTask).getId();
        testUpdateTaskRequest.setTaskId(id);
        try {
            mvc.perform(put("/api/task").contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testUpdateTaskRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("new title"))
                    .andExpect(jsonPath("$.description").value("new description"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateTaskThrowsException() {
        testUpdateTaskRequest.setTaskId(6L);
        try {
            mvc.perform(put("/api/task").contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testUpdateTaskRequest)))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllTasksReturnsDtoList() {
        taskRepository.save(testTask);
        try {
            mvc.perform(get("/api/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].title").value(testTaskResponse.getTitle()))
                    .andExpect(jsonPath("$[0].description").value(testTaskResponse.getDescription()))
                    .andExpect(jsonPath("$[0].userId").value(testTaskResponse.getUserId()))
                    .andExpect(jsonPath("$[0].status").value(testTaskResponse.getStatus().toString()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deleteTaskReturnsMessage() {
        Long id = taskRepository.save(testTask).getId();
        try {
            mvc.perform(delete("/api/task/{taskId}", id))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Task removed by ID: " + id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
}