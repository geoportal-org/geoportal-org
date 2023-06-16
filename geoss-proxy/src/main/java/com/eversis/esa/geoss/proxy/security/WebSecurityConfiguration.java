package com.eversis.esa.geoss.proxy.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * The type Web security configuration.
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private final JwtAuthConverter jwtAuthConverter;

    /**
     * Instantiates a new Web security configuration.
     *
     * @param jwtAuthConverter the jwt auth converter
     */
    public WebSecurityConfiguration(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
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
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http, RepositoryRestConfiguration repositoryRestConfiguration) throws Exception {
        final String basePath = repositoryRestConfiguration.getBasePath().toString();
        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, basePath + "/log/**").hasAnyRole("log_writer")
                .requestMatchers(HttpMethod.GET, basePath + "/popular/**").permitAll()
                .requestMatchers(HttpMethod.GET, basePath + "/statistics/**").hasAnyRole("statistics_reader")
                .anyRequest().authenticated();
        http.oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthConverter);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }

}
