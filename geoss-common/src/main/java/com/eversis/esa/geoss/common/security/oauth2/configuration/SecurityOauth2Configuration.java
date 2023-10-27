package com.eversis.esa.geoss.common.security.oauth2.configuration;

import com.eversis.esa.geoss.common.security.oauth2.ClaimAccessorGrantedAuthoritiesConverter;
import com.eversis.esa.geoss.common.security.oauth2.JwtGrantedAuthoritiesConverter;
import com.eversis.esa.geoss.common.security.oauth2.OidcGrantedAuthoritiesMapper;
import com.eversis.esa.geoss.common.security.oauth2.OidcUserAuthorityGrantedAuthoritiesConverter;
import com.eversis.esa.geoss.common.security.oauth2.properties.SecurityOauth2Properties;

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
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * The type Open api oauth2 configuration.
 */
@ConditionalOnProperty(prefix = "spring.security.oauth2", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(SecurityOauth2Properties.class)
@Configuration(proxyBeanMethods = false)
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
}
