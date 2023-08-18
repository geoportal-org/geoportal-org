package com.eversis.esa.geoss.curated.resources.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The type Resources configuration.
 */
@EnableElasticsearchRepositories(basePackages = "com.eversis.esa.geoss.curated.resources.elasticsearch.repository")
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.curated.resources.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.resources.controller",
                "com.eversis.esa.geoss.curated.resources.service.impl",
                "com.eversis.esa.geoss.curated.resources.mapper",
                "com.eversis.esa.geoss.curated.resources.elasticsearch.service.impl",
                "com.eversis.esa.geoss.curated.resources.elasticsearch.mapper"
        }
)
@PropertySource("classpath:application-resources.properties")
@Configuration(proxyBeanMethods = false)
public class ResourcesConfiguration {

    /**
     * The Username.
     */
    @Value("${keycloak.admin}")
    String username;

    /**
     * The Password.
     */
    @Value("${keycloak.admin.password}")
    String password;

    /**
     * The Server url.
     */
    @Value("${spring.security.oauth2.base.uri}")
    String serverUrl;

    /**
     * Keycloak client keycloak.
     *
     * @return the keycloak
     */
    @Bean
    public Keycloak keycloakClient() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")
                .grantType("password")
                .username(username)
                .password(password)
                .clientId("admin-cli")
                .build();
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
