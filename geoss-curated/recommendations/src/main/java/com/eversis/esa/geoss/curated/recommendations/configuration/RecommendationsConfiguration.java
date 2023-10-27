package com.eversis.esa.geoss.curated.recommendations.configuration;

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
 * The type Recommendations configuration.
 */
@Log4j2
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.curated.recommendations.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.recommendations.controller",
                "com.eversis.esa.geoss.curated.recommendations.security",
                "com.eversis.esa.geoss.curated.recommendations.service.internal",
                "com.eversis.esa.geoss.curated.recommendations.support.internal"
        }
)
@PropertySource("classpath:application-recommendations.properties")
@Configuration(proxyBeanMethods = false)
public class RecommendationsConfiguration {

    @PostConstruct
    void init() {
        log.debug("Init module");
    }

    /**
     * Recommendations open api customizer open api customizer.
     *
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomizer recommendationsOpenApiCustomizer() {
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
                    .flatMap(pathItem -> Stream.of(pathItem.getPost(), pathItem.getPut(), pathItem.getDelete()));
            operations.forEach(operation -> {
                if (operation != null) {
                    List<String> tags = operation.getTags();
                    if (tags != null) {
                        if (tags.contains("recommendations")) {
                            operation.setSecurity(securityRequirements);
                        }
                    }
                }
            });
        };
    }
}
