package org.homework.taskmanagerservice.mapper;

import org.homework.taskmanagerservice.dto.request.NewTaskRequest;
import org.homework.taskmanagerservice.dto.response.TaskResponse;
import org.homework.taskmanagerservice.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Task toTask(NewTaskRequest request);

    TaskResponse toTaskResponse(Task task);

    List<TaskResponse> toTaskResponseList(List<Task> all);
}
