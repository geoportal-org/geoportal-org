package com.eversis.esa.geoss.curatedrelationships.search.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TreeMap;

/**
 * The type Open api configuration.
 */
@OpenAPIDefinition(info = @Info(title = "OpenAPI definition", version = "v0", description = "GEOSS Search API"))
@Configuration(proxyBeanMethods = false)
public class OpenApiConfiguration {

    /**
     * Sort schemas open api customizer open api customizer.
     *
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomiser sortSchemasOpenApiCustomizer() {
        return openApi -> {
            Components components = openApi.getComponents();
            if (components != null && components.getSchemas() != null) {
                components.setSchemas(new TreeMap<>(components.getSchemas()));
            }
        };
    }
}
