package org.homework.taskmanagerservice.exception;

public class TaskNotFoundException extends RuntimeException{
    private static final String TASK_NOT_FOUND = "Task with provided Id not found";

    public TaskNotFoundException() {super(TASK_NOT_FOUND);}
}
