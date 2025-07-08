package com.patient.management.controller;

import com.patient.management.payload.CreateUserDTO;
import com.patient.management.service.user.UserFxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserFxService userService;

    @Autowired
    public UserController(UserFxService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-admin")
    public ResponseEntity<String> createInitialAdmin(@RequestBody CreateUserDTO createUserDTO) {
        try {
            userService.createInitialAdmin(createUserDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Admin user created successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create admin user");
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getUserCount() {
        Integer count = userService.getUserCount();
        return ResponseEntity.ok(count);
    }
}