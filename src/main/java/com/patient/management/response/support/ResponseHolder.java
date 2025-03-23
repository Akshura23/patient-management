package com.patient.management.response.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseHolder<T> {
    private String code;
    private String message;
    private T data;
}
