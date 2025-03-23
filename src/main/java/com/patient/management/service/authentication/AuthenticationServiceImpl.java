package com.patient.management.service.authentication;

import com.patient.management.entity.UserEntity;
import com.patient.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(String username, String password) {
        UserEntity user = userRepository.findByUsername(username);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    public void registerUser(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        UserEntity user = new UserEntity(username, encodedPassword);
        userRepository.save(user);
    }
}
