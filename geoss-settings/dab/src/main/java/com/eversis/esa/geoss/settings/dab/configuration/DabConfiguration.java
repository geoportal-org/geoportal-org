package com.eversis.esa.geoss.settings.dab.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The type Dab configuration.
 */
@ComponentScan(basePackages = "com.eversis.esa.geoss.settings.dab")
@PropertySource("classpath:application-dab.properties")
@Configuration(proxyBeanMethods = false)
public class DabConfiguration {

    /**
     * Dab open api customizer open api customizer.
     *
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomizer dabOpenApiCustomizer() {
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
                        if (tags.contains("dabs")) {
                            operation.setSecurity(securityRequirements);
                        }
                    }
                }
            });
        };
    }
}
