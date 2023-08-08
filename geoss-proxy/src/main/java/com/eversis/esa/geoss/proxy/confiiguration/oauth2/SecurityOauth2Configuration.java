package com.eversis.esa.geoss.proxy.confiiguration.oauth2;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * The type Open api oauth2 configuration.
 */
@SecuritySchemes(
        value = {
                @SecurityScheme(
                        name = "Authorization",
                        type = SecuritySchemeType.OAUTH2,
                        flows = @OAuthFlows(
                                authorizationCode = @OAuthFlow(
                                        authorizationUrl = "${openapi.oauth2.oauth-flow.authorizationUrl}",
                                        tokenUrl = "${openapi.oauth2.oauth-flow.tokenUrl}",
                                        refreshUrl = "${openapi.oauth2.oauth-flow.refreshUrl}",
                                        scopes = {
                                                @OAuthScope(name = "openid", description = "OpenID Connect"),
                                                @OAuthScope(name = "profile",
                                                            description = "OpenID Connect scope profile"),
                                                @OAuthScope(name = "roles",
                                                            description = "OpenID Connect scope for add user roles to "
                                                                          + "the access token")
                                        }
                                ))
                )
        }
)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.security.oauth2", name = "enabled", havingValue = "true")
public class SecurityOauth2Configuration {

}
