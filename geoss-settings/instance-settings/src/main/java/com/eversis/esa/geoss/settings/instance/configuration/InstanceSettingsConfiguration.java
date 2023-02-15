package com.eversis.esa.geoss.settings.instance.configuration;

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
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.settings.instance.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.settings.instance.controller",
                "com.eversis.esa.geoss.settings.instance.event"
        }
)
@Configuration(proxyBeanMethods = false)
public class InstanceSettingsConfiguration {

    /**
     * System settings open api customizer open api customizer.
     *
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomizer instanceSettingsOpenApiCustomizer() {
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
                    List<String> tags = operation.getTags();
                    if (tags != null && tags.contains("default-layers")) {
                        operation.setSecurity(securityRequirements);
                    }
                }
            });
        };
    }
}
