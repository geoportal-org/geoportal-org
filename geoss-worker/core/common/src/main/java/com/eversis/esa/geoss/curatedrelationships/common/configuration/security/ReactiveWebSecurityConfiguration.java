package com.eversis.esa.geoss.curatedrelationships.common.configuration.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import java.util.Set;

/**
 * The type Reactive web security configuration.
 */
@Log4j2
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ReactiveWebSecurityConfiguration {

    private final Set<ApiSecurityWebFilterChainCustomizer> apiSecurityWebFilterChainCustomizers;

    private final Set<DefaultSecurityWebFilterChainCustomizer> defaultSecurityWebFilterChainCustomizers;

    /**
     * Swagger ui security web filter chain security web filter chain.
     *
     * @param http the http
     * @return the security web filter chain
     */
    @Bean
    @ConditionalOnProperty(prefix = "springdoc.swagger-ui", name = "enabled", havingValue = "true")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityWebFilterChain swaggerUiSecurityWebFilterChain(ServerHttpSecurity http) {
        http.securityMatcher(ServerWebExchangeMatchers.pathMatchers("/v3/api-docs/**", "/swagger-ui/**"));
        http.authorizeExchange(
                authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers("/v3/api-docs/**", "/swagger-ui/**")
                        .authenticated());
        http.formLogin(Customizer.withDefaults());
        return http.build();
    }

    /**
     * Test security web filter chain security web filter chain.
     *
     * @param http the http
     * @return the security web filter chain
     */
    @Bean
    @Order(SecurityProperties.DEFAULT_FILTER_ORDER)
    SecurityWebFilterChain testSecurityWebFilterChain(ServerHttpSecurity http) {
        http.securityMatcher(ServerWebExchangeMatchers.pathMatchers("/ping"));
        http.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers("/ping").permitAll());
        return http.build();
    }

    /**
     * Api security web filter chain security web filter chain.
     *
     * @param http the http
     * @return the security web filter chain
     */
    @Bean
    @Order(SecurityProperties.DEFAULT_FILTER_ORDER)
    SecurityWebFilterChain apiSecurityWebFilterChain(ServerHttpSecurity http) {
        http.httpBasic(Customizer.withDefaults());
        http.csrf(CsrfSpec::disable);
        http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        for (ApiSecurityWebFilterChainCustomizer customizer : apiSecurityWebFilterChainCustomizers) {
            customizer.customize(http);
        }
        return http.build();
    }

    /**
     * Error security web filter chain security web filter chain.
     *
     * @param http the http
     * @return the security web filter chain
     */
    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    SecurityWebFilterChain errorSecurityWebFilterChain(ServerHttpSecurity http) {
        http.securityMatcher(ServerWebExchangeMatchers.pathMatchers("/error"));
        http.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers("/error").permitAll());
        return http.build();
    }

    /**
     * Default security web filter chain security web filter chain.
     *
     * @param http the http
     * @return the security web filter chain
     */
    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    SecurityWebFilterChain defaultSecurityWebFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.anyExchange().authenticated());
        if (!defaultSecurityWebFilterChainCustomizers.isEmpty()) {
            for (DefaultSecurityWebFilterChainCustomizer customizer : defaultSecurityWebFilterChainCustomizers) {
                customizer.customize(http);
            }
        } else {
            http.formLogin(Customizer.withDefaults());
            http.logout(Customizer.withDefaults());
        }
        return http.build();
    }
}
