package com.eversis.esa.geoss.contents.configuration;

import com.eversis.esa.geoss.contents.domain.Document;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ExposureConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

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
                                || tags.contains("menus") || tags.contains("pages") || tags.contains("sites")) {
                            operation.setSecurity(securityRequirements);
                        }
                    }
                }
            });
            // add schemas
            final StringSchema stringSchema = new StringSchema();
            final List<String> locales = List.of("en", "es", "fr", "pl", "ru", "zh");

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

            StringSchema binaryStringSchema = new StringSchema();
            binaryStringSchema.format("binary");
            StringSchema modelStringSchema = new StringSchema();
            modelStringSchema.setExample("{\"title\":\"example\",\"fileName\":\"example.png\",\"extension\":\"png\""
                                         + ",\"path\":\"0\",\"folderId\":0}");
            ArraySchema filesSchema = new ArraySchema();
            filesSchema.items(binaryStringSchema);
            ObjectSchema documentRequestBodySchema = new ObjectSchema();
            documentRequestBodySchema.addProperty("files", filesSchema);
            documentRequestBodySchema.addProperty("model", modelStringSchema);
            openApi.schema("DocumentRequestBody", documentRequestBodySchema);
        };
    }

    /**
     * Contents repository rest configurer repository rest configurer.
     *
     * @return the repository rest configurer
     */
    @Bean
    RepositoryRestConfigurer contentsRepositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration repositoryRestConfiguration,
                    CorsRegistry cors) {
                ExposureConfiguration exposureConfiguration = repositoryRestConfiguration.getExposureConfiguration();
                exposureConfiguration.forDomainType(Document.class)
                        .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(HttpMethod.POST));
            }
        };
    }
}
