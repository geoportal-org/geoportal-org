package com.eversis.esa.geoss.personaldata.survey.configuration;

import com.eversis.esa.geoss.personaldata.survey.domain.Survey;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
import java.util.stream.Stream;

/**
 * The type Survey configuration.
 */
@Log4j2
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.personaldata.survey.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.personaldata.survey.controller",
                "com.eversis.esa.geoss.personaldata.survey.event",
                "com.eversis.esa.geoss.personaldata.survey.mapper",
                "com.eversis.esa.geoss.personaldata.survey.service.internal",
        }
)
@Configuration(proxyBeanMethods = false)
public class SurveyConfiguration {

    /**
     * Survey open api customizer open api customizer.
     *
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomizer surveyOpenApiCustomizer() {
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
                        if (tags.contains("surveys")) {
                            operation.setSecurity(securityRequirements);
                        }
                    }
                    if ("postCollectionResource-survey-post".equals(operation.getOperationId())) {
                        operation.setSecurity(null);
                    }
                }
            });
        };
    }

    /**
     * Survey repository rest configurer repository rest configurer.
     *
     * @return the repository rest configurer
     */
    @Bean
    RepositoryRestConfigurer surveyRepositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration repositoryRestConfiguration,
                    CorsRegistry cors) {
                ExposureConfiguration exposureConfiguration = repositoryRestConfiguration.getExposureConfiguration();
                exposureConfiguration.forDomainType(Survey.class)
                        .withItemExposure(
                                (metadata, httpMethods) -> httpMethods.disable(HttpMethod.PATCH, HttpMethod.PUT));
            }
        };
    }
}
