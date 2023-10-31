package com.eversis.esa.geoss.contents.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Contents configuration.
 */
@Configuration(proxyBeanMethods = false)
public class ContentsConfiguration {

    /**
     * Contens open api customizer open api customizer.
     *
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomizer contensOpenApiCustomizer() {
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
                        if (tags.contains("contents") || tags.contains("documents") || tags.contains("folders")
                                || tags.contains("menus") || tags.contains("pages")) {
                            operation.setSecurity(securityRequirements);
                        }
                    }
                }
            });
            // add schemas
            final StringSchema stringSchema = new StringSchema();
            final List<String> locales = List.of("en", "es");

            MapSchema contentLocalizedTitleSchema = new MapSchema();
            contentLocalizedTitleSchema.setProperties(
                    locales.stream().collect(Collectors.toMap(k -> k, v -> stringSchema)));
            contentLocalizedTitleSchema.setAdditionalProperties(stringSchema);
            contentLocalizedTitleSchema.setExample(
                    locales.stream().collect(Collectors.toMap(k -> k, v -> "Lorem ipsum")));
            openApi.schema("ContentLocalizedTitle", contentLocalizedTitleSchema);
            MapSchema contentLocalizedDataSchema = new MapSchema();
            contentLocalizedDataSchema.setProperties(
                    locales.stream().collect(Collectors.toMap(k -> k, v -> stringSchema)));
            contentLocalizedDataSchema.setAdditionalProperties(stringSchema);
            contentLocalizedDataSchema.setExample(
                    locales.stream().collect(Collectors.toMap(k -> k, v -> "Lorem ipsum dolor sit amet")));
            openApi.schema("ContentLocalizedData", contentLocalizedDataSchema);

            MapSchema menuLocalizedTitleSchema = new MapSchema();
            menuLocalizedTitleSchema.setProperties(
                    locales.stream().collect(Collectors.toMap(k -> k, v -> stringSchema)));
            menuLocalizedTitleSchema.setAdditionalProperties(stringSchema);
            menuLocalizedTitleSchema.setExample(
                    locales.stream().collect(Collectors.toMap(k -> k, v -> "Lorem ipsum")));
            openApi.schema("MenuLocalizedTitle", menuLocalizedTitleSchema);
            MapSchema menuLocalizedImageSchema = new MapSchema();
            menuLocalizedImageSchema.setProperties(
                    locales.stream().collect(Collectors.toMap(k -> k, v -> stringSchema)));
            menuLocalizedImageSchema.setAdditionalProperties(stringSchema);
            menuLocalizedImageSchema.setExample(
                    locales.stream().collect(Collectors.toMap(k -> k, v -> "Lorem ipsum dolor sit amet")));
            openApi.schema("MenuLocalizedImageTitle", menuLocalizedImageSchema);

            MapSchema pageLocalizedTitleSchema = new MapSchema();
            pageLocalizedTitleSchema.setProperties(
                    locales.stream().collect(Collectors.toMap(k -> k, v -> stringSchema)));
            pageLocalizedTitleSchema.setAdditionalProperties(stringSchema);
            pageLocalizedTitleSchema.setExample(
                    locales.stream().collect(Collectors.toMap(k -> k, v -> "Lorem ipsum")));
            openApi.schema("PageLocalizedTitle", pageLocalizedTitleSchema);
            MapSchema pageLocalizedDescriptionSchema = new MapSchema();
            pageLocalizedDescriptionSchema.setProperties(
                    locales.stream().collect(Collectors.toMap(k -> k, v -> stringSchema)));
            pageLocalizedDescriptionSchema.setAdditionalProperties(stringSchema);
            pageLocalizedDescriptionSchema.setExample(
                    locales.stream().collect(Collectors.toMap(k -> k, v -> "Lorem ipsum dolor sit amet")));
            openApi.schema("PageLocalizedDescription", pageLocalizedDescriptionSchema);
        };
    }
}
