package com.github.gabrielsmartins.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundError(ResourceNotFoundException ex) {
        var error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleError(BusinessRuleException ex) {
        var error = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Business Rule Conflict",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ErrorResponse.FieldValidationError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ErrorResponse.FieldValidationError(err.getField(), err.getDefaultMessage()))
                .toList();

        var error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.",
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        var error = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Access Denied",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // Tratamento genérico para erros não mapeados (Garante que a API nunca retorne o HTML padrão do Spring)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        var error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocorreu um erro inesperado no servidor."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}