package com.eversis.esa.geoss.curated.elasticsearch.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The type Elasticsearch configuration.
 */
@Log4j2
@EnableElasticsearchRepositories(basePackages = "com.eversis.esa.geoss.curated.elasticsearch.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.elasticsearch.event",
                "com.eversis.esa.geoss.curated.elasticsearch.mapper",
                "com.eversis.esa.geoss.curated.elasticsearch.service.impl",
                "com.eversis.esa.geoss.curated.elasticsearch.controller"
        }
)
@PropertySource("classpath:application-elasticsearch.properties")
@Configuration(proxyBeanMethods = false)
public class ElasticsearchConfiguration {

    /**
     * Init.
     */
    @PostConstruct
    void init() {
        log.debug("Init module");
    }

    /**
     * Elasticsearch open api customizer open api customizer.
     *
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomizer elasticsearchOpenApiCustomizer() {
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
                        if (tags.contains("elasticsearch")) {
                            operation.setSecurity(securityRequirements);
                        }
                    }
                }
            });
        };
    }
}
