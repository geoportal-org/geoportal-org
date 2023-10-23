package com.eversis.esa.geoss.curated.resources.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The type Resources configuration.
 */
@Log4j2
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.curated.resources.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.resources.controller",
                "com.eversis.esa.geoss.curated.resources.service.impl",
                "com.eversis.esa.geoss.curated.resources.mapper"
        }
)
@PropertySource("classpath:application-resources.properties")
@Configuration(proxyBeanMethods = false)
public class ResourcesConfiguration {

    @PostConstruct
    void init() {
        log.warn("Init module");
    }

    /**
     * Resources open api customizer open api customizer.
     *
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomizer resourcesOpenApiCustomizer() {
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

            Stream<Operation> operations = openApi.getPaths().values().stream()
                    .flatMap(pathItem -> pathItem.readOperations().stream());
            operations.forEach(operation -> {
                if (operation != null) {
                    List<String> tags = operation.getTags();
                    if (tags != null) {
                        if (tags.contains("accessPolicy") || tags.contains("elasticsearch")
                                || tags.contains("organisation") || tags.contains("resources")
                                || tags.contains("source") || tags.contains("type")
                                || tags.contains("userResources") || tags.contains("workflow")) {
                            operation.setSecurity(securityRequirements);
                        }
                    }
                }
            });
        };
    }

}
