package com.patient.management.exception;

import com.patient.management.enums.ExceptionCode;
import com.patient.management.response.error.ErrorDetails;

import java.util.Collections;

public class ClientConnectionException extends BaseRuntimeException {
    public ClientConnectionException() {
        super(new ErrorDetails(ExceptionCode.CLIENT_CONNECTION_EXCEPTION, Collections.singletonList("Client connection error!")));
    }
}
