package com.patient.management.exception;

import com.patient.management.response.error.ErrorDetails;
import com.patient.management.response.error.FieldErrorDetails;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
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
    public void handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
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

        showAlert("Validation Error", "Failed Validation", errorDetailsList.toString());
    }

    @ExceptionHandler(BaseRuntimeException.class)
    public void handleValidationException(BaseRuntimeException baseRuntimeException) {
        ErrorDetails error = baseRuntimeException.getErrorDetails();
        String correlationId = MDC.get("CORRELATION-ID");
        log.error("CorrelationId{}, BusinessFieldValidationException {}", correlationId, error.getErrorMessages());

        showAlert("Validation Error", "Business Field Validation Exception", error.getErrorMessages().toString());
    }

    @ExceptionHandler(Exception.class)
    public void handleResourceNotFountException(Exception ex) {
        String correlationId = MDC.get("CORRELATION-ID");
        log.error("CorrelationId{} , An unexpected error occurred: {}", correlationId, ex.getMessage());

        showAlert("Unexpected Error", "An unexpected error occurred", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(IllegalArgumentException ex) {
        String correlationId = MDC.get("CORRELATION-ID");
        log.error("CorrelationId{} ,Illegal Argument Exception: {}", correlationId, ex.getMessage());

        showAlert("Illegal Argument", "Illegal Argument Exception", ex.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public void handleDataAccessException(DataAccessException ex) {
        String correlationId = MDC.get("CORRELATION-ID");
        log.error("CorrelationId{} , Database Exception: {}", correlationId, ex.getMessage());

        showAlert("Database Error", "A database error occurred", "Please try again later.");
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}