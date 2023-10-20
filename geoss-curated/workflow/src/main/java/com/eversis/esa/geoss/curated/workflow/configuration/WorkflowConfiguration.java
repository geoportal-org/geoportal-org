package com.eversis.esa.geoss.curated.workflow.configuration;

import com.eversis.esa.geoss.curated.workflow.properties.KeycloakProperties;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The type Workflow configuration.
 */
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.workflow.controller",
                "com.eversis.esa.geoss.curated.workflow.service.impl",
        }
)
@PropertySource("classpath:application-workflow.properties")
@Configuration(proxyBeanMethods = false)
public class WorkflowConfiguration {

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
