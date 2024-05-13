package com.eversis.esa.geoss.settings.instance.configuration;

import com.eversis.esa.geoss.settings.instance.domain.CatalogOption;
import com.eversis.esa.geoss.settings.instance.domain.ViewOption;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Instance settings configuration.
 */
@Log4j2
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.settings.instance.repository")
@ComponentScan(basePackages = "com.eversis.esa.geoss.settings.instance")
@Configuration(proxyBeanMethods = false)
public class InstanceSettingsConfiguration {

    /**
     * Instance settings open api customizer open api customizer.
     *
     * @param locales the locales
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomizer instanceSettingsOpenApiCustomizer(
            @Value("${com.eversis.esa.geoss.settings.supported-languages}") List<String> locales) {
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
                    .flatMap(pathItem -> Stream.of(pathItem.getPost(), pathItem.getPut(), pathItem.getDelete(),
                            pathItem.getPatch()));
            operations.forEach(operation -> {
                if (operation != null) {
                    List<String> tags = operation.getTags();
                    if (tags != null) {
                        if (tags.contains("catalogs") || tags.contains("default-layers") || tags.contains(
                                "tutorial-tags") || tags.contains("views")) {
                            operation.setSecurity(securityRequirements);
                        }
                    }
                }
            });

            // add schemas
            final StringSchema stringSchema = new StringSchema();

            MapSchema tagLocalizedTitleSchema = new MapSchema();
            tagLocalizedTitleSchema.setProperties(
                    locales.stream().collect(Collectors.toMap(k -> k, v -> stringSchema)));
            tagLocalizedTitleSchema.setAdditionalProperties(stringSchema);
            tagLocalizedTitleSchema.setExample(
                    locales.stream().collect(Collectors.toMap(k -> k, v -> "Lorem ipsum")));
            openApi.schema("TagLocalizedTitle", tagLocalizedTitleSchema);

            MapSchema tagLocalizedDescriptionSchema = new MapSchema();
            tagLocalizedDescriptionSchema.setProperties(
                    locales.stream().collect(Collectors.toMap(k -> k, v -> stringSchema)));
            tagLocalizedDescriptionSchema.setAdditionalProperties(stringSchema);
            tagLocalizedDescriptionSchema.setExample(
                    locales.stream().collect(Collectors.toMap(k -> k, v -> "Lorem ipsum dolor sit amet")));
            openApi.schema("TagLocalizedDescription", tagLocalizedDescriptionSchema);

            ModelConverters modelConverters = ModelConverters.getInstance();
            ResolvedSchema catalogOptionSchema = modelConverters.resolveAsResolvedSchema(
                    new AnnotatedType(CatalogOption.class));
            openApi.schema("CatalogOption", catalogOptionSchema.schema);
            ResolvedSchema viewOptionSchema = modelConverters.resolveAsResolvedSchema(
                    new AnnotatedType(ViewOption.class));
            openApi.schema("ViewOption", viewOptionSchema.schema);

            // override schemas
            Optional.ofNullable(openApi.getComponents())
                    .flatMap(components -> Optional.ofNullable(components.getSchemas()))
                    .map(Map::values)
                    .orElse(Collections.emptySet())
                    .forEach(schema -> {
                        String name = schema.getName();
                        if ("EntityModelCatalog".equals(name) || "CatalogRequestBody".equals(name)) {
                            schema.addProperty("subOptions", new ArraySchema().items(
                                    new StringSchema().$ref(AnnotationsUtils.COMPONENTS_REF + "ViewOption")));
                        }
                        if ("EntityModelView".equals(name) || "ViewRequestBody".equals(name)) {
                            schema.addProperty("subOptions", new ArraySchema().items(
                                    new StringSchema().$ref(AnnotationsUtils.COMPONENTS_REF + "ViewOption")));
                        }
                    });
        };
    }
}
