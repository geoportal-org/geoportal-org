package com.eversis.esa.geoss.curatedrelationships.common.configuration.security.oauth2;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;

/**
 * The type Security oauth 2 configuration.
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
public class SecurityOauth2Configuration {

    /**
     * Simple authority mapper simple authority mapper.
     *
     * @return the simple authority mapper
     */
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
     * @param securityOauth2Properties the security oauth 2 properties
     * @return the claim accessor granted authorities converter
     */
    @Bean
    ClaimAccessorGrantedAuthoritiesConverter claimAccessorGrantedAuthoritiesConverter(
            SecurityOauth2Properties securityOauth2Properties) {
        ClaimAccessorGrantedAuthoritiesConverter claimAccessorGrantedAuthoritiesConverter
                = new ClaimAccessorGrantedAuthoritiesConverter();
        claimAccessorGrantedAuthoritiesConverter.setClientId(securityOauth2Properties.getClientId());
        claimAccessorGrantedAuthoritiesConverter.setSimpleAuthorityMapper(simpleAuthorityMapper());
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
     * Reactive jwt authentication converter reactive jwt authentication converter.
     *
     * @param claimAccessorGrantedAuthoritiesConverter the claim accessor granted authorities converter
     * @return the reactive jwt authentication converter
     */
    @Bean
    ReactiveJwtAuthenticationConverter reactiveJwtAuthenticationConverter(
            ClaimAccessorGrantedAuthoritiesConverter claimAccessorGrantedAuthoritiesConverter) {
        ReactiveJwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
                new ReactiveJwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setClaimAccessorGrantedAuthoritiesConverter(
                claimAccessorGrantedAuthoritiesConverter);

        ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
