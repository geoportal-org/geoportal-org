package com.eversis.esa.geoss.settings.system.configuration;

import com.eversis.esa.geoss.settings.system.domain.ApiSettingsSet;
import com.eversis.esa.geoss.settings.system.domain.WebSettingsSet;
import com.eversis.esa.geoss.settings.system.support.StringToApiSettingsKeyConverter;
import com.eversis.esa.geoss.settings.system.support.StringToApiSettingsSetConverter;
import com.eversis.esa.geoss.settings.system.support.StringToRegionalSettingsKeyConverter;
import com.eversis.esa.geoss.settings.system.support.StringToWebSettingsKeyConverter;
import com.eversis.esa.geoss.settings.system.support.StringToWebSettingsSetConverter;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.BaseUri;
import org.springframework.data.rest.webmvc.ProfileController;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The type System settings configuration.
 */
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.settings.system.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.settings.system.controller",
                "com.eversis.esa.geoss.settings.system.event",
                "com.eversis.esa.geoss.settings.system.service",
                "com.eversis.esa.geoss.settings.system.support"
        }
)
@Configuration(proxyBeanMethods = false)
public class SystemSettingsConfiguration {

    /**
     * System settings open api customizer open api customizer.
     *
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomizer systemSettingsOpenApiCustomizer() {
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
                        if (tags.contains("api-settings") || tags.contains("web-settings") || tags.contains(
                                "regional-settings")) {
                            operation.setSecurity(securityRequirements);
                        }
                    }
                    // override params
                    String operationId = operation.getOperationId();
                    if ("executeSearch-apisettings-get".equals(operationId)) {
                        List<Parameter> parameters = operation.getParameters();
                        if (parameters != null) {
                            for (Parameter parameter : parameters) {
                                if ("key".equals(parameter.getName())) {
                                    if (parameter.getSchema() instanceof StringSchema stringSchema) {
                                        stringSchema.setEnum(ApiSettingsSet.keys());
                                    }
                                }
                            }
                        }
                    }
                    if ("executeSearch-websettings-get".equals(operationId)) {
                        List<Parameter> parameters = operation.getParameters();
                        if (parameters != null) {
                            for (Parameter parameter : parameters) {
                                if ("key".equals(parameter.getName())) {
                                    if (parameter.getSchema() instanceof StringSchema stringSchema) {
                                        stringSchema.setEnum(WebSettingsSet.keys());
                                    }
                                }
                            }
                        }
                    }
                }
            });
            // override schemas
            Optional.ofNullable(openApi.getComponents())
                    .flatMap(components -> Optional.ofNullable(components.getSchemas()))
                    .map(Map::values)
                    .orElse(Collections.emptySet())
                    .forEach(schema -> {
                        String name = schema.getName();
                        if ("EntityModelApiSettings".equals(name) || "ApiSettingsRequestBody".equals(name)) {
                            Optional.ofNullable(schema.getProperties())
                                    .map(map -> map.get("key"))
                                    .map(o -> {
                                        if (o instanceof StringSchema stringSchema) {
                                            stringSchema.setEnum(ApiSettingsSet.keys());
                                        }
                                        return o;
                                    });
                        }
                        if ("EntityModelWebSettings".equals(name) || "WebSettingsRequestBody".equals(name)) {
                            Optional.ofNullable(schema.getProperties())
                                    .map(map -> map.get("key"))
                                    .map(o -> {
                                        if (o instanceof StringSchema stringSchema) {
                                            stringSchema.setEnum(WebSettingsSet.keys());
                                        }
                                        return o;
                                    });
                        }
                    });
        };
    }

    /**
     * System settings repository rest configurer repository rest configurer.
     *
     * @return the repository rest configurer
     */
    @Bean
    RepositoryRestConfigurer systemSettingsRepositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureConversionService(ConfigurableConversionService conversionService) {
                conversionService.addConverter(new StringToApiSettingsKeyConverter());
                conversionService.addConverter(new StringToApiSettingsSetConverter());
                conversionService.addConverter(new StringToWebSettingsKeyConverter());
                conversionService.addConverter(new StringToWebSettingsSetConverter());
                conversionService.addConverter(new StringToRegionalSettingsKeyConverter());
            }
        };
    }

    /**
     * System settings profile controller contributor representation model processor.
     *
     * @param baseUri the base uri
     * @return the representation model processor
     */
    @Bean
    RepresentationModelProcessor<RepresentationModel<?>> systemSettingsProfileControllerContributor(BaseUri baseUri) {
        return model -> {
            if (model.getClass().isAssignableFrom(RepresentationModel.class)) {
                String regionalSettings = baseUri.getUriComponentsBuilder()
                        .path(ProfileController.PROFILE_ROOT_MAPPING)
                        .path("/regional-settings").toUriString();
                model.add(Link.of(regionalSettings, "regionalSettings"));
            }
            return model;
        };
    }
}
