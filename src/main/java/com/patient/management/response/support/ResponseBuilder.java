package com.patient.management.response.support;

import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ToString
public class ResponseBuilder<T> {
    private HttpStatus status;
    private String code;
    private String message;
    private T data;

    public static <U> ResponseBuilder<U> builder(U result) {
        return new ResponseBuilder<U>().data(result);
    }

    public ResponseBuilder<T> code(String code) {
        this.code = code;
        return this;
    }

    public ResponseBuilder<T> message(String message) {
        this.message = message;
        return this;
    }

    public ResponseBuilder<T> data(T data) {
        this.data = data;
        return this;
    }

    public ResponseBuilder<T> status(HttpStatus status) {
        this.status = status;
        return this;
    }

    public ResponseEntity<ResponseHolder<T>> build() {
        ResponseHolder<T> body = new ResponseHolder<>(code, message, data);
        return new ResponseEntity<>(body,status);
    }
}
