package com.github.gabrielsmartins.taskmanager.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message,
        List<FieldValidationError> validations
) {
    public ErrorResponse(Integer status, String error, String message) {
        this(LocalDateTime.now(), status, error, message, null);
    }

    public ErrorResponse(Integer status, String error, String message, List<FieldValidationError> validations) {
        this(LocalDateTime.now(), status, error, message, validations);
    }

    public record FieldValidationError(String field, String message) {}
}