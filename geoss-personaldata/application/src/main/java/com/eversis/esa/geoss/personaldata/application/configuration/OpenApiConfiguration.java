package com.eversis.esa.geoss.personaldata.application.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * The type Open api configuration.
 */
@OpenAPIDefinition(info = @Info(title = "OpenAPI definition", version = "v0", description = "GEOSS PersonalData API"))
@SecuritySchemes(
        value = {
                @SecurityScheme(name = "Basic",
                                scheme = "basic",
                                type = SecuritySchemeType.HTTP)
        }
)
@Configuration(proxyBeanMethods = false)
public class OpenApiConfiguration {

    /**
     * Profile operation open api customizer open api customizer.
     *
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomizer profileOperationOpenApiCustomizer() {
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

            Stream<Operation> operations = openApi.getPaths().values().stream().map(PathItem::getGet);
            operations.forEach(operation -> {
                if (operation != null) {
                    List<String> tags = operation.getTags();
                    if (tags != null) {
                        if (tags.contains("profile-controller")) {
                            operation.setSecurity(securityRequirements);
                        }
                    }
                }
            });
        };
    }

    /**
     * Sort schemas open api customizer open api customizer.
     *
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomizer sortSchemasOpenApiCustomizer() {
        return openApi -> {
            Components components = openApi.getComponents();
            if (components != null && components.getSchemas() != null) {
                components.setSchemas(new TreeMap<>(components.getSchemas()));
            }
        };
    }
}
