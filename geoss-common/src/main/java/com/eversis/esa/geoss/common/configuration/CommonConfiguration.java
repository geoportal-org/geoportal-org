package com.eversis.esa.geoss.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * The type Common configuration.
 */
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.common.boot",
                "com.eversis.esa.geoss.common.controller",
                "com.eversis.esa.geoss.common.event",
                "com.eversis.esa.geoss.common.exception",
                "com.eversis.esa.geoss.common.hateoas",
                "com.eversis.esa.geoss.common.rest",
                "com.eversis.esa.geoss.common.security.configuration",
                "com.eversis.esa.geoss.common.security.oauth2.configuration"
        }
)
@Configuration(proxyBeanMethods = false)
public class CommonConfiguration {

    private static final String DEFAULT_AUDITOR_NAME = "xxxxxxxx-xxxx-cron-xxxx-xxxxxxxxxxxx";

    /**
     * Auditor aware.
     *
     * @return the auditor aware
     */
    @Bean
    AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                .or(() -> Optional.of(DEFAULT_AUDITOR_NAME));
    }
}
