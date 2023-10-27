package com.eversis.esa.geoss.common.security.oauth2.configuration;

import com.eversis.esa.geoss.common.security.ApiSecurityFilterChainCustomizer;
import com.eversis.esa.geoss.common.security.DefaultSecurityFilterChainCustomizer;
import com.eversis.esa.geoss.common.security.oauth2.properties.SecurityOauth2Properties;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * The type Web security oauth 2 configuration.
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
@EnableConfigurationProperties(SecurityOauth2Properties.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.security.oauth2", name = "enabled", havingValue = "true")
public class WebSecurityOauth2Configuration {

    /**
     * Oidc logout success handler logout success handler.
     *
     * @param clientRegistrationRepository the client registration repository
     * @param securityOauth2Properties the security oauth 2 properties
     * @return the logout success handler
     */
    @Bean
    LogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository,
            SecurityOauth2Properties securityOauth2Properties) {
        OidcClientInitiatedLogoutSuccessHandler oidcClientInitiatedLogoutSuccessHandler
                = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        oidcClientInitiatedLogoutSuccessHandler.setPostLogoutRedirectUri(
                securityOauth2Properties.getPostLogoutRedirectUri());
        return oidcClientInitiatedLogoutSuccessHandler;
    }

    /**
     * Oauth 2 default security filter chain customizer default security filter chain customizer.
     *
     * @param oidcLogoutSuccessHandler the oidc logout success handler
     * @return the default security filter chain customizer
     */
    @Order(SecurityProperties.DEFAULT_FILTER_ORDER)
    @Bean
    DefaultSecurityFilterChainCustomizer oauth2defaultSecurityFilterChainCustomizer(
            LogoutSuccessHandler oidcLogoutSuccessHandler) {
        return http -> {
            try {
                http.oauth2Login(Customizer.withDefaults());
                http.oauth2Client(Customizer.withDefaults());
                http.logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .logoutSuccessHandler(oidcLogoutSuccessHandler));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Oauth 2 api security filter chain customizer api security filter chain customizer.
     *
     * @return the api security filter chain customizer
     */
    @Order(SecurityProperties.DEFAULT_FILTER_ORDER)
    @Bean
    ApiSecurityFilterChainCustomizer oauth2apiSecurityFilterChainCustomizer() {
        return http -> {
            try {
                http.httpBasic(AbstractHttpConfigurer::disable);
                http.oauth2ResourceServer(
                        httpSecurityOAuth2ResourceServerConfigurer -> httpSecurityOAuth2ResourceServerConfigurer
                                .jwt(Customizer.withDefaults()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
