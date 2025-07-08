package com.patient.management.exception;

import com.patient.management.response.error.ErrorDetails;
import com.patient.management.response.error.FieldErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class CustomGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String correlationId = MDC.get("CORRELATION-ID");
        Map<String, List<String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())
                ));

        List<FieldErrorDetails> errorDetailsList = fieldErrors.entrySet().stream()
                .map(entry -> new FieldErrorDetails(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        log.error("CorrelationId{}, Errors: {}", correlationId, errorDetailsList);

        Map<String, Object> response = Map.of(
                "error", "Validation Failed",
                "message", "Request validation failed",
                "details", errorDetailsList
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(BaseRuntimeException baseRuntimeException) {
        ErrorDetails error = baseRuntimeException.getErrorDetails();
        String correlationId = MDC.get("CORRELATION-ID");
        log.error("CorrelationId{}, BusinessFieldValidationException {}", correlationId, error.getErrorMessages());

        Map<String, Object> response = Map.of(
                "error", "Business Validation Error",
                "message", "Business field validation failed",
                "details", error.getErrorMessages()
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFountException(Exception ex) {
        String correlationId = MDC.get("CORRELATION-ID");
        log.error("CorrelationId{} , An unexpected error occurred: {}", correlationId, ex.getMessage());

        Map<String, Object> response = Map.of(
                "error", "Internal Server Error",
                "message", "An unexpected error occurred",
                "details", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        String correlationId = MDC.get("CORRELATION-ID");
        log.error("CorrelationId{} ,Illegal Argument Exception: {}", correlationId, ex.getMessage());

        Map<String, Object> response = Map.of(
                "error", "Bad Request",
                "message", "Illegal argument provided",
                "details", ex.getMessage()
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException ex) {
        String correlationId = MDC.get("CORRELATION-ID");
        log.error("CorrelationId{} , Database Exception: {}", correlationId, ex.getMessage());

        Map<String, Object> response = Map.of(
                "error", "Database Error",
                "message", "A database error occurred",
                "details", "Please try again later."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}