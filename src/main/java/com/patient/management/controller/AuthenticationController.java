package com.patient.management.controller;

import com.patient.management.payload.LoginRequestDTO;
import com.patient.management.payload.LoginResponseDTO;
import com.patient.management.service.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        boolean isAuthenticated = authenticationService.authenticate(
                loginRequest.getUsername(), 
                loginRequest.getPassword()
        );
        
        if (isAuthenticated) {
            return ResponseEntity.ok(new LoginResponseDTO(
                    true, 
                    "Login successful", 
                    loginRequest.getUsername()
            ));
        } else {
            return ResponseEntity.badRequest().body(new LoginResponseDTO(
                    false, 
                    "Invalid username or password", 
                    null
            ));
        }
    }
}