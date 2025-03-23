package com.patient.management.exception;

import com.patient.management.response.error.ErrorDetails;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class BaseRuntimeException extends RuntimeException{
    private final ErrorDetails errorDetails;
    private Object data;

    public BaseRuntimeException(final ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }
    public BaseRuntimeException(final ErrorDetails errorDetails, final Object data) {
        this.data = data;
        this.errorDetails = errorDetails;
    }
}

