package com.eversis.esa.geoss.curatedrelationships.search.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * The type Application configuration.
 */
@Log4j2
@EnableAsync
@EnableScheduling
@Configuration(proxyBeanMethods = false)
public class ApplicationConfiguration {

    /**
     * Auditor aware auditor aware.
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
