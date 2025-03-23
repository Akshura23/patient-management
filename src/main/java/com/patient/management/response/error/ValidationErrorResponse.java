package com.patient.management.response.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class ValidationErrorResponse {
    private HttpStatus status;
    private String exceptionCode;
    private List<FieldErrorDetails> errors;
}