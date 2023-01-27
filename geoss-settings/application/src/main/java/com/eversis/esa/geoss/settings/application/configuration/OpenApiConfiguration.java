package com.eversis.esa.geoss.settings.application.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Stream;

/**
 * The type Open api configuration.
 */
@OpenAPIDefinition(info = @Info(title = "OpenAPI definition", version = "v0", description = "GEOSS Settings API"))
@SecuritySchemes(
        value = {
                @SecurityScheme(name = "Basic",
                                scheme = "basic",
                                type = SecuritySchemeType.HTTP,
                                in = SecuritySchemeIn.HEADER)
        }
)
@Configuration(proxyBeanMethods = false)
public class OpenApiConfiguration {

    /**
     * Profile operation open api customizer open api customizer.
     *
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomizer profileOperationOpenApiCustomizer() {
        return openApi -> {
            Stream<Operation> operations = openApi.getPaths().values().stream().map(PathItem::getGet);
            operations.forEach(operation -> {
                if (operation != null) {
                    String operationId = operation.getOperationId();
                    if (operationId.startsWith("listAllFormsOfMetadata_") || operationId.startsWith("descriptor_")) {
                        SecurityRequirement securityRequirement = new SecurityRequirement();
                        securityRequirement.addList("Basic");
                        operation.addSecurityItem(securityRequirement);
                    }
                }
            });
        };
    }
}
