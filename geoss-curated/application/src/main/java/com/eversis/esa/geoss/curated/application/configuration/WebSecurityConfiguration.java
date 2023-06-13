package com.eversis.esa.geoss.curated.application.configuration;

import com.eversis.esa.geoss.curated.application.configuration.oauth2.SecurityOauth2Properties;

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

    private final ObjectProvider<PersistentTokenRepository> persistentTokenRepository;

    private final ObjectProvider<LogoutSuccessHandler> oidcLogoutSuccessHandler;

    private final Optional<SecurityOauth2Properties> securityOauth2Properties;

    /**
     * H 2 console security filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.h2.console", name = "enabled", havingValue = "true")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher(PathRequest.toH2Console());
        http.authorizeHttpRequests(
                authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers(PathRequest.toH2Console()).authenticated()
        );
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer
                .ignoringRequestMatchers(PathRequest.toH2Console()));
        http.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                .frameOptions(FrameOptionsConfig::sameOrigin));
        http.formLogin(Customizer.withDefaults());
        return http.build();
    }

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
                    .requestMatchers(HttpMethod.GET, basePath + "/comments/**")
                    .hasAnyRole("COMMENT_READER", "ADMIN");
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.DELETE, basePath + "/comments/**")
                    .hasAnyRole("COMMENT_REMOVER", "ADMIN");
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(basePath + "/comments/**")
                    .hasAnyRole("COMMENT_WRITER", "ADMIN");
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET, basePath + "/highlighted-searches/search/enabled")
                    .permitAll();
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET, basePath + "/highlighted-searches/**")
                    .hasAnyRole("HIGHLIGHTED_SEARCHES_READER", "ADMIN");
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.DELETE, basePath + "/highlighted-searches/**")
                    .hasAnyRole("HIGHLIGHTED_SEARCHES_REMOVER", "ADMIN");
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(basePath + "/highlighted-searches/**")
                    .hasAnyRole("HIGHLIGHTED_SEARCHES_WRITER", "ADMIN");
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET, basePath + "/saved-searches/search/current")
                    .hasAnyRole("SAVED_SEARCHES_MANAGER", "ADMIN");
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET, basePath + "/saved-searches/search/**")
                    .hasAnyRole("SAVED_SEARCHES_READER", "ADMIN");
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET, basePath + "/saved-searches")
                    .hasAnyRole("SAVED_SEARCHES_READER", "ADMIN");
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET, basePath + "/saved-searches/**")
                    .hasAnyRole("SAVED_SEARCHES_MANAGER", "SAVED_SEARCHES_READER", "ADMIN");
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.DELETE, basePath + "/saved-searches/**")
                    .hasAnyRole("SAVED_SEARCHES_MANAGER", "SAVED_SEARCHES_REMOVER", "ADMIN");
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(basePath + "/saved-searches/**")
                    .hasAnyRole("SAVED_SEARCHES_MANAGER", "ADMIN");
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET, basePath + "/surveys/**")
                    .hasAnyRole("SURVEY_READER", "ADMIN");
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.DELETE, basePath + "/surveys/**")
                    .hasAnyRole("SURVEY_REMOVER", "ADMIN");
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.POST, basePath + "/surveys/**")
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
            http.rememberMe(httpSecurityRememberMeConfigurer -> httpSecurityRememberMeConfigurer
                    .tokenRepository(persistentTokenRepository.getIfAvailable()));
            http.logout(Customizer.withDefaults());
        }
        return http.build();
    }
}
