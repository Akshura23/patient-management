package com.patient.management.response.error;

import com.patient.management.enums.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ErrorDetails {
    private ExceptionCode exceptionCode;
    private List<String> errorMessages;

}
