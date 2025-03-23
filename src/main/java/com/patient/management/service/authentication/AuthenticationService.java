package com.patient.management.service.authentication;

public interface AuthenticationService {
    boolean authenticate(String username, String password);
}
