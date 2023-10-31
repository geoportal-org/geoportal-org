package com.eversis.esa.geoss.contents.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * The type Application configuration.
 */
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@Configuration(proxyBeanMethods = false)
public class ApplicationConfiguration {

    /**
     * Auditor aware auditor.
     *
     * @return the auditor aware
     */
    @Bean
    AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName);
    }
}
