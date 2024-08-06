package com.eversis.esa.geoss.curated.workflow.configuration;

import com.eversis.esa.geoss.curated.workflow.properties.KeycloakProperties;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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

    /**
     * Init.
     */
    @PostConstruct
    void init() {
        log.debug("Init module");
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

    /**
     * Workflow open api customizer open api customizer.
     *
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomizer workflowOpenApiCustomizer() {
        return openApi -> {
            List<SecurityRequirement> securityRequirements = Optional.ofNullable(openApi.getComponents())
                    .map(Components::getSecuritySchemes)
                    .filter(Objects::nonNull)
                    .map(Map::keySet)
                    .orElse(Collections.emptySet())
                    .stream()
                    .map(s -> {
                        SecurityRequirement securityRequirement = new SecurityRequirement();
                        securityRequirement.addList(s);
                        return securityRequirement;
                    }).toList();
            // add security schemas to operations
            Stream<Operation> operations = openApi.getPaths().values().stream()
                    .flatMap(pathItem -> pathItem.readOperations().stream());
            operations.forEach(operation -> {
                if (operation != null) {
                    List<String> tags = operation.getTags();
                    if (tags != null) {
                        if (tags.contains("workflow")) {
                            operation.setSecurity(securityRequirements);
                        }
                    }
                }
            });
        };
    }
}
