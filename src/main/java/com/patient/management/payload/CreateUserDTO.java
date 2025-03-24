package com.patient.management.payload;

import lombok.Data;

import java.time.LocalDate;
@Data
public class CreateUserDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private LocalDate birthday;
}
