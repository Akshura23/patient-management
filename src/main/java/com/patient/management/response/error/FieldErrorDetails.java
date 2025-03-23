package com.patient.management.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@ToString
public class FieldErrorDetails {
    private String field;
    private List<String> messages;
}