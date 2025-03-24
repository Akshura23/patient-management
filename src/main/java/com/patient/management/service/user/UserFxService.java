package com.patient.management.service.user;

import com.patient.management.payload.CreateUserDTO;

public interface UserFxService {
    void createInitialAdmin(CreateUserDTO createUserDTO);
    Integer getUserCount();
}
