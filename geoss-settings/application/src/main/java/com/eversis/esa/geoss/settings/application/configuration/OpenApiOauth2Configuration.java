package com.eversis.esa.geoss.settings.application.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Stream;

/**
 * The type Open api oauth2 configuration.
 */
@SecuritySchemes(
        value = {
                @SecurityScheme(name = "Authorization",
                                type = SecuritySchemeType.OAUTH2,
                                flows = @OAuthFlows(
                                        authorizationCode = @OAuthFlow(
                                                authorizationUrl = "${springdoc.oauth2.oauth-flow.authorizationUrl}",
                                                tokenUrl = "${springdoc.oauth2.oauth-flow.tokenUrl}"
                                        ))
                )
        }
)
@ConditionalOnProperty(prefix = "spring.security.oauth2", name = "enabled", havingValue = "true")
@Configuration(proxyBeanMethods = false)
public class OpenApiOauth2Configuration {

    /**
     * Profile operation open api customizer open api customizer.
     *
     * @return the open api customizer
     */
    @Bean
    OpenApiCustomizer profileOperationOpenApiOauth2Customizer() {
        return openApi -> {
            Stream<Operation> operations = openApi.getPaths().values().stream().map(PathItem::getGet);
            operations.forEach(operation -> {
                if (operation != null) {
                    String operationId = operation.getOperationId();
                    if (operationId.startsWith("listAllFormsOfMetadata_") || operationId.startsWith("descriptor_")) {
                        SecurityRequirement securityRequirement = new SecurityRequirement();
                        securityRequirement.addList("Authorization");
                        operation.addSecurityItem(securityRequirement);
                    }
                }
            });
        };
    }
}
