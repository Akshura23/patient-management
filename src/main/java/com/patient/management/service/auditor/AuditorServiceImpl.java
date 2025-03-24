package com.patient.management.service.auditor;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorServiceImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable("SAdmin");
    }
}
