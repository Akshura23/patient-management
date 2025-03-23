package com.patient.management.enums;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    CLIENT_CONNECTION_EXCEPTION( "EX-001"),
    FAILED_VALIDATION("EX-002"),
    BUSINESS_VALIDATION("EX-003"),
    INTERNAL_SERVER_ERROR("EX-004"),
    EXPECTED_VALIDATION_CODE("EX-005"),
    UPDATE_VALIDATION("EX-006"),
    AUTH_SERVER_ERROR("EX-007"),
    CREATE_VALIDATION("EX-008"),
    ILLEGAL_STATE_EXCEPTION( "EX-009"),
    PATCH_VALIDATION("EX-010"),
    DATABASE_ERROR( "EX-011");

    private final String code;

    ExceptionCode(String code) {
        this.code = code;
    }
}