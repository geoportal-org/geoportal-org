package com.eversis.esa.geoss.settings.site.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The type Site configuration.
 */
@ComponentScan(basePackages = "com.eversis.esa.geoss.settings.site")
@Configuration(proxyBeanMethods = false)
public class SiteConfiguration {

    /**
     * Site open api customizer open api customizer.
     *
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomizer siteOpenApiCustomizer() {
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
                    .flatMap(pathItem -> Stream.of(pathItem.getOptions(), pathItem.getPost(), pathItem.getPut(),
                            pathItem.getDelete(), pathItem.getPatch()));
            operations.forEach(operation -> {
                if (operation != null) {
                    List<String> tags = operation.getTags();
                    if (tags != null) {
                        if (tags.contains("site")) {
                            operation.setSecurity(securityRequirements);
                        }
                    }
                }
            });
        };
    }
}
