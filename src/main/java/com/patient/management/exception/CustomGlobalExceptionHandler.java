package com.patient.management.exception;

import com.patient.management.enums.ExceptionCode;
import com.patient.management.response.error.ErrorDetails;
import com.patient.management.response.error.FieldErrorDetails;
import com.patient.management.response.error.GlobalErrorResponse;
import com.patient.management.response.error.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class CustomGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
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

        return new ResponseEntity<>(ValidationErrorResponse
                .builder()
                .status(HttpStatus.PRECONDITION_FAILED)
                .exceptionCode(ExceptionCode.FAILED_VALIDATION.getCode())
                .errors(errorDetailsList)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<GlobalErrorResponse> handleValidationException(BaseRuntimeException baseRuntimeException) {
        ErrorDetails error = baseRuntimeException.getErrorDetails();
        String correlationId = MDC.get("CORRELATION-ID");
        log.error("CorrelationId{}, BusinessFieldValidationException {}", correlationId, error.getErrorMessages());
        return new ResponseEntity<>(GlobalErrorResponse
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .exceptionCode(String.valueOf(error.getExceptionCode().getCode()))
                .errors(error.getErrorMessages())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalErrorResponse> handleResourceNotFountException(Exception ex) {
        String correlationId = MDC.get("CORRELATION-ID");
        log.error("CorrelationId{} , An unexpected error occurred: {}", correlationId, ex.getMessage());
        return new ResponseEntity<>(GlobalErrorResponse
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .exceptionCode(ExceptionCode.INTERNAL_SERVER_ERROR.getCode())
                .errors(Collections.singletonList(ex.getMessage()))
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        String correlationId = MDC.get("CORRELATION-ID");
        log.error("CorrelationId{} ,Illegal Argument Exception: {}", correlationId, ex.getMessage());
        return new ResponseEntity<>(GlobalErrorResponse
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .exceptionCode(ExceptionCode.EXPECTED_VALIDATION_CODE.getCode())
                .errors(Collections.singletonList(ex.getMessage()))
                .build(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<GlobalErrorResponse> handleDataAccessException(DataAccessException ex) {
        String correlationId = MDC.get("CORRELATION-ID");
        log.error("CorrelationId{} , Database Exception: {}", correlationId, ex.getMessage());
        return new ResponseEntity<>(GlobalErrorResponse
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .exceptionCode(ExceptionCode.DATABASE_ERROR.getCode())
                .errors(Collections.singletonList("A database error occurred. Please try again later."))
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
