package com.eversis.esa.geoss.curated.workflow.configuration;

import com.eversis.esa.geoss.curated.workflow.properties.KeycloakProperties;

import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import jakarta.annotation.PostConstruct;

/**
 * The type Workflow configuration.
 */
@Log4j2
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.workflow.controller",
                "com.eversis.esa.geoss.curated.workflow.service.impl",
        }
)
@PropertySource("classpath:application-workflow.properties")
@Configuration(proxyBeanMethods = false)
public class WorkflowConfiguration {

    @PostConstruct
    void init() {
        log.warn("Init module");
    }

    /**
     * Keycloak keycloak.
     *
     * @param keycloakProperties the keycloak properties
     * @return the keycloak
     */
    @ConditionalOnProperty(prefix = "keycloak.admin", name = "server-url")
    @Bean
    Keycloak keycloak(KeycloakProperties keycloakProperties) {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getServerUrl())
                .realm("master")
                .grantType("password")
                .username(keycloakProperties.getUsername())
                .password(keycloakProperties.getPassword())
                .clientId("admin-cli")
                .build();
    }
}
