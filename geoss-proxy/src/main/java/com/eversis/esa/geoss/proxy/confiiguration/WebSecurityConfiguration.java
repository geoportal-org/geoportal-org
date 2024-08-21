package com.eversis.esa.geoss.proxy.confiiguration;

import com.eversis.esa.geoss.proxy.confiiguration.oauth2.SecurityOauth2Properties;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.rest.webmvc.BaseUri;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Optional;

/**
 * The type Web security configuration.
 */
@Log4j2
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class WebSecurityConfiguration {

    private final BaseUri baseUri;

    private final Optional<PersistentTokenRepository> persistentTokenRepository;

    private final ObjectProvider<LogoutSuccessHandler> oidcLogoutSuccessHandler;

    private final Optional<SecurityOauth2Properties> securityOauth2Properties;

    /**
     * Swagger ui security filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    @ConditionalOnProperty(prefix = "springdoc.swagger-ui", name = "enabled", havingValue = "true")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain swaggerUiSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/v3/api-docs/**", "/swagger-ui/**");
        http.authorizeHttpRequests(
                authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").authenticated());
        http.formLogin(Customizer.withDefaults());
        return http.build();
    }

    /**
     * Test security filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    @Order(SecurityProperties.DEFAULT_FILTER_ORDER)
    SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/ping");
        http.authorizeHttpRequests(
                authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers("/ping").permitAll());
        return http.build();
    }

    /**
     * Api security filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    @Order(SecurityProperties.DEFAULT_FILTER_ORDER)
    SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        final String basePath = baseUri.getUri().toString();
        http.securityMatcher(basePath + "/**");
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET, basePath + "/popular/**")
                    .permitAll();
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.POST, basePath + "/statistics/**")
                    .hasAnyRole("statistics_reader");
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.POST, basePath + "/log/**")
                    .permitAll();
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.POST, basePath + "/counter/view/**")
                    .permitAll();
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET, basePath + "/counter/view/**")
                    .permitAll();
            authorizationManagerRequestMatcherRegistry
                    .anyRequest().authenticated();
        });
        if (securityOauth2Properties.map(SecurityOauth2Properties::isEnabled).orElse(false)) {
            http.oauth2ResourceServer(
                    httpSecurityOAuth2ResourceServerConfigurer -> httpSecurityOAuth2ResourceServerConfigurer
                            .jwt(Customizer.withDefaults()));
        } else {
            http.httpBasic(Customizer.withDefaults());
        }
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(
                httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    /**
     * Error security filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    SecurityFilterChain errorSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/error");
        http.authorizeHttpRequests(
                authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers("/error").permitAll());
        return http.build();
    }

    /**
     * Default security filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .anyRequest().authenticated());
        if (securityOauth2Properties.map(SecurityOauth2Properties::isEnabled).orElse(false)) {
            http.oauth2Login(Customizer.withDefaults());
            http.oauth2Client(Customizer.withDefaults());
            http.logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                    .logoutSuccessHandler(oidcLogoutSuccessHandler.getIfAvailable()));
        } else {
            http.formLogin(Customizer.withDefaults());
            if (persistentTokenRepository.isPresent()) {
                http.rememberMe(httpSecurityRememberMeConfigurer -> httpSecurityRememberMeConfigurer
                        .tokenRepository(persistentTokenRepository.get()));
            }
            http.logout(Customizer.withDefaults());
        }
        return http.build();
    }
}
