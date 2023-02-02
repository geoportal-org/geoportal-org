package com.eversis.esa.geoss.settings.application.configuration.oauth2;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

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
                                        authorizationUrl = "${springdoc.oauth2.oauth-flow.authorizationUrl}",
                                        tokenUrl = "${springdoc.oauth2.oauth-flow.tokenUrl}"
                                ))
                )
        }
)
@EnableConfigurationProperties(SecurityOauth2Properties.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.security.oauth2", name = "enabled", havingValue = "true")
public class SecurityOauth2Configuration {

    /**
     * Simple authority mapper simple authority mapper.
     *
     * @return the simple authority mapper
     */
    @Bean
    SimpleAuthorityMapper simpleAuthorityMapper() {
        SimpleAuthorityMapper simpleAuthorityMapper = new SimpleAuthorityMapper();
        simpleAuthorityMapper.setPrefix("ROLE_");
        simpleAuthorityMapper.setConvertToUpperCase(true);
        simpleAuthorityMapper.afterPropertiesSet();
        return simpleAuthorityMapper;
    }

    /**
     * Claim accessor granted authorities converter claim accessor granted authorities converter.
     *
     * @param simpleAuthorityMapper the simple authority mapper
     * @return the claim accessor granted authorities converter
     */
    @Bean
    ClaimAccessorGrantedAuthoritiesConverter claimAccessorGrantedAuthoritiesConverter(
            SimpleAuthorityMapper simpleAuthorityMapper) {
        ClaimAccessorGrantedAuthoritiesConverter claimAccessorGrantedAuthoritiesConverter
                = new ClaimAccessorGrantedAuthoritiesConverter();
        claimAccessorGrantedAuthoritiesConverter.setAuthoritiesClaimName("realm_access");
        claimAccessorGrantedAuthoritiesConverter.setAuthoritiesClaimAttributeName("roles");
        claimAccessorGrantedAuthoritiesConverter.setSimpleAuthorityMapper(simpleAuthorityMapper);
        return claimAccessorGrantedAuthoritiesConverter;
    }

    /**
     * Granted authorities mapper granted authorities mapper.
     *
     * @param claimAccessorGrantedAuthoritiesConverter the claim accessor granted authorities converter
     * @return the granted authorities mapper
     */
    @Bean
    GrantedAuthoritiesMapper grantedAuthoritiesMapper(
            ClaimAccessorGrantedAuthoritiesConverter claimAccessorGrantedAuthoritiesConverter) {
        OidcUserAuthorityGrantedAuthoritiesConverter oidcUserAuthorityGrantedAuthoritiesConverter
                = new OidcUserAuthorityGrantedAuthoritiesConverter();
        oidcUserAuthorityGrantedAuthoritiesConverter.setClaimAccessorGrantedAuthoritiesConverter(
                claimAccessorGrantedAuthoritiesConverter);

        OidcGrantedAuthoritiesMapper oidcGrantedAuthoritiesMapper = new OidcGrantedAuthoritiesMapper();
        oidcGrantedAuthoritiesMapper.setOidcUserAuthorityGrantedAuthoritiesConverter(
                oidcUserAuthorityGrantedAuthoritiesConverter);

        return oidcGrantedAuthoritiesMapper;
    }

    /**
     * Jwt authentication converter jwt authentication converter.
     *
     * @param claimAccessorGrantedAuthoritiesConverter the claim accessor granted authorities converter
     * @return the jwt authentication converter
     */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(
            ClaimAccessorGrantedAuthoritiesConverter claimAccessorGrantedAuthoritiesConverter) {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setClaimAccessorGrantedAuthoritiesConverter(
                claimAccessorGrantedAuthoritiesConverter);

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

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
}
