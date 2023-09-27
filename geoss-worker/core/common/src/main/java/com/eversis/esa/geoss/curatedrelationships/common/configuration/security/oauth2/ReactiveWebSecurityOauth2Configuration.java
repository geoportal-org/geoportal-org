package com.eversis.esa.geoss.curatedrelationships.common.configuration.security.oauth2;

import com.eversis.esa.geoss.curatedrelationships.common.configuration.security.ApiSecurityWebFilterChainCustomizer;
import com.eversis.esa.geoss.curatedrelationships.common.configuration.security.DefaultSecurityWebFilterChainCustomizer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpBasicSpec;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

/**
 * The type Reactive web security oauth 2 configuration.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.security.oauth2", name = "enabled", havingValue = "true")
@ConditionalOnWebApplication(type = Type.REACTIVE)
public class ReactiveWebSecurityOauth2Configuration {

    /**
     * Oidc server logout success handler server logout success handler.
     *
     * @param reactiveClientRegistrationRepository the reactive client registration repository
     * @param securityOauth2Properties the security oauth 2 properties
     * @return the server logout success handler
     */
    @Bean
    ServerLogoutSuccessHandler oidcServerLogoutSuccessHandler(
            ReactiveClientRegistrationRepository reactiveClientRegistrationRepository,
            SecurityOauth2Properties securityOauth2Properties) {
        OidcClientInitiatedServerLogoutSuccessHandler oidcClientInitiatedLogoutSuccessHandler
                = new OidcClientInitiatedServerLogoutSuccessHandler(reactiveClientRegistrationRepository);
        oidcClientInitiatedLogoutSuccessHandler.setPostLogoutRedirectUri(
                securityOauth2Properties.getPostLogoutRedirectUri());
        return oidcClientInitiatedLogoutSuccessHandler;
    }

    /**
     * Default security web filter chain customizer default security web filter chain customizer.
     *
     * @param oidcServerLogoutSuccessHandler the oidc server logout success handler
     * @return the default security web filter chain customizer
     */
    @Bean
    DefaultSecurityWebFilterChainCustomizer oauth2DefaultSecurityWebFilterChainCustomizer(
            ServerLogoutSuccessHandler oidcServerLogoutSuccessHandler) {
        return http -> {
            http.oauth2Login(Customizer.withDefaults());
            http.oauth2Client(Customizer.withDefaults());
            http.logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                    .logoutSuccessHandler(oidcServerLogoutSuccessHandler));
        };
    }

    /**
     * Api security web filter chain customizer api security web filter chain customizer.
     *
     * @return the api security web filter chain customizer
     */
    @Bean
    ApiSecurityWebFilterChainCustomizer oauth2SecurityWebFilterChainCustomizer() {
        return http -> {
            http.httpBasic(HttpBasicSpec::disable);
            http.oauth2ResourceServer(
                    httpSecurityOAuth2ResourceServerConfigurer -> httpSecurityOAuth2ResourceServerConfigurer
                            .jwt(Customizer.withDefaults()));
        };
    }
}
