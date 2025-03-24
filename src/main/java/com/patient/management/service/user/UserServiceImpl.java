package com.patient.management.service.user;

import com.patient.management.entity.UserEntity;
import com.patient.management.entity.role.Role;
import com.patient.management.payload.CreateUserDTO;
import com.patient.management.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserFxService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createInitialAdmin(CreateUserDTO dto) {
        if (userRepository.count() > 0) {
            throw new IllegalStateException("Initial admin can only be created when no users exist");
        }

        UserEntity admin = new UserEntity(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getUsername(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getBirthday(),
                Role.ADMIN
        );
        admin.setCreatedBy("system");

        userRepository.save(admin);
    }
    @Override
    @Transactional
    public Integer getUserCount() {
        return Math.toIntExact(userRepository.count());
    }
}
