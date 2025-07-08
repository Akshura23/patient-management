package com.patient.management.payload;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String username;
    private String password;
}