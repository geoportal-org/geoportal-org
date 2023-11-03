package com.eversis.esa.geoss.curated.recommendations.security;

import com.eversis.esa.geoss.common.security.ApiSecurityFilterChainCustomizer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.rest.webmvc.BaseUri;

/**
 * The type Recommendations security configuration.
 */
@Log4j2
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class RecommendationsSecurityConfiguration {

    private final BaseUri baseUri;

    /**
     * Recommendations api security web filter chain customizer api security filter chain customizer.
     *
     * @return the api security filter chain customizer
     */
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    @Bean
    ApiSecurityFilterChainCustomizer recommendationsApiSecurityWebFilterChainCustomizer() {
        return http -> {
            final String basePath = baseUri.getUri().toString();
            try {
                http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(basePath + "/recommendations/**")
                            // delegate authorization to method access-control
                            .permitAll();
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
