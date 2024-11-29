package org.homework.taskmanagerservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessage {
    BINDING_RESULTS_ERROR("Bad request parameters");

    private final String message;
}
