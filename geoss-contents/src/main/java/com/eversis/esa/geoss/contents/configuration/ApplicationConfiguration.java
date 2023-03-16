package com.eversis.esa.geoss.contents.configuration;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * The type Application configuration.
 */
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@Configuration
public class ApplicationConfiguration {

    /**
     * Auditor aware auditor.
     *
     * @return the auditor aware
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("Administrator");
    }

}
