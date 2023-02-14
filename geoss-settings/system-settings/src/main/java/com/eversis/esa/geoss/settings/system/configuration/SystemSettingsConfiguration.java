package com.eversis.esa.geoss.settings.system.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * The type System settings configuration.
 */
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.settings.system.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.settings.system.controller",
                "com.eversis.esa.geoss.settings.system.event"
        }
)
@Configuration(proxyBeanMethods = false)
public class SystemSettingsConfiguration {

    /**
     * System settings open api customizer open api customizer.
     *
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomizer systemSettingsOpenApiCustomizer() {
        return openApi -> {
            Set<String> securitySchemes = Optional.ofNullable(openApi.getComponents())
                    .map(Components::getSecuritySchemes)
                    .filter(Objects::nonNull)
                    .map(Map::keySet)
                    .orElse(Collections.emptySet());
            List<SecurityRequirement> securityRequirements = securitySchemes.stream()
                    .map(s -> {
                        SecurityRequirement securityRequirement = new SecurityRequirement();
                        securityRequirement.addList(s);
                        return securityRequirement;
                    }).toList();

            Stream<Operation> operations = openApi.getPaths().values().stream()
                    .flatMap(pathItem -> pathItem.readOperations().stream());
            operations.forEach(operation -> {
                if (operation != null) {
                    String operationId = operation.getOperationId();
                    if (operationId.contains("apisettings") || operationId.contains("ApiSettings")) {
                        operation.setSecurity(securityRequirements);
                    }
                }
            });
        };
    }
}
