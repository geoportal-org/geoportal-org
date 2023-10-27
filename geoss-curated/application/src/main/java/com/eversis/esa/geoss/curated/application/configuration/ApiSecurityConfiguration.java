package com.eversis.esa.geoss.curated.application.configuration;

import com.eversis.esa.geoss.common.security.ApiSecurityFilterChainCustomizer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.BaseUri;

/**
 * The type Api security configuration.
 */
@Log4j2
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ApiSecurityConfiguration {

    private final BaseUri baseUri;

    /**
     * Api security filter chain security filter chain.
     *
     * @return the security filter chain
     */
    @Bean
    ApiSecurityFilterChainCustomizer apiSecurityWebFilterChainCustomizer() {
        return http -> {
            final String basePath = baseUri.getUri().toString();
            http.securityMatcher(basePath + "/**");
            try {
                http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry
                            .anyRequest().authenticated();
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
