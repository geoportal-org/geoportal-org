package com.eversis.esa.geoss.settings.application.configuration;

import com.eversis.esa.geoss.settings.application.configuration.oauth2.SecurityOauth2Properties;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Optional;

/**
 * The type Web security configuration.
 */
@Log4j2

@Configuration(proxyBeanMethods = false)
public class WebSecurityConfiguration {

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
        http.authorizeHttpRequests().requestMatchers(PathRequest.toH2Console()).authenticated();
        http.csrf().ignoringRequestMatchers(PathRequest.toH2Console());
        http.headers().frameOptions().sameOrigin();
        http.formLogin();
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
        http.authorizeHttpRequests()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").authenticated();
        http.formLogin();
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
        http.authorizeHttpRequests().requestMatchers("/ping").permitAll();
        return http.build();
    }

    /**
     * Api security filter chain security filter chain.
     *
     * @param http the http
     * @param repositoryRestConfiguration the repository rest configuration
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    @Order(SecurityProperties.DEFAULT_FILTER_ORDER)
    SecurityFilterChain apiSecurityFilterChain(HttpSecurity http,
            RepositoryRestConfiguration repositoryRestConfiguration,
            Optional<SecurityOauth2Properties> securityOauth2Properties) throws Exception {
        final String basePath = repositoryRestConfiguration.getBasePath().toString();
        http.securityMatcher(basePath + "/**");
        http.authorizeHttpRequests()
                .requestMatchers(basePath + "/api-settings/**").hasRole("ADMIN")
                .requestMatchers(basePath + "/layers/**").hasRole("ADMIN")
                .anyRequest().authenticated();
        http.csrf().disable();
        http.httpBasic();
        if (securityOauth2Properties.map(SecurityOauth2Properties::isEnabled).orElse(false)) {
            http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        }
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
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
        http.authorizeHttpRequests().requestMatchers("/error").permitAll();
        return http.build();
    }

    /**
     * Default security filter chain security filter chain.
     *
     * @param http the http
     * @param persistentTokenRepository the persistent token repository
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
            PersistentTokenRepository persistentTokenRepository,
            Optional<SecurityOauth2Properties> securityOauth2Properties,
            ObjectProvider<LogoutSuccessHandler> oidcLogoutSuccessHandler
    ) throws Exception {
        http.authorizeHttpRequests().anyRequest().authenticated();
        http.httpBasic();
        http.formLogin();
        if (securityOauth2Properties.map(SecurityOauth2Properties::isEnabled).orElse(false)) {
            http.oauth2Login();
            http.oauth2Client();
            http.logout().logoutSuccessHandler(oidcLogoutSuccessHandler.getIfAvailable());
        } else {
            http.rememberMe().tokenRepository(persistentTokenRepository);
            http.logout();
        }
        return http.build();
    }
}
