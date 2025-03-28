package com.patient.management.configuration;

import com.patient.management.service.auditor.AuditorServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditorConfig {
    @Bean
    public AuditorAware<String> auditorAware(){
        return new AuditorServiceImpl();
    }
}
