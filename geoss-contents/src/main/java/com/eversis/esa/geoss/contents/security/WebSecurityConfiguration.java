package com.eversis.esa.geoss.contents.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * The type Web security configuration.
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private static final String NOOP_PASSWORD_PREFIX = "{noop}";

    private static final Pattern PASSWORD_ALGORITHM_PATTERN = Pattern.compile("^\\{.+}.*$");

    private final JwtAuthConverter jwtAuthConverter;

    private final SecurityProperties securityProperties;

    /**
     * Instantiates a new Web security configuration.
     *
     * @param jwtAuthConverter the jwt auth converter
     * @param securityProperties the security properties
     */
    public WebSecurityConfiguration(JwtAuthConverter jwtAuthConverter, SecurityProperties securityProperties) {
        this.jwtAuthConverter = jwtAuthConverter;
        this.securityProperties = securityProperties;
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
     * Api security filter chain security filter chain.
     *
     * @param http the http
     * @param repositoryRestConfiguration the repository rest configuration
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    @Order(SecurityProperties.DEFAULT_FILTER_ORDER)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http,
            RepositoryRestConfiguration repositoryRestConfiguration) throws Exception {
        final String basePath = repositoryRestConfiguration.getBasePath().toString();
        http.securityMatcher(basePath + "/**");
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.GET, basePath + "/content/**").permitAll()
                    .requestMatchers(HttpMethod.POST, basePath + "/content/**").hasAnyRole("content_writer")
                    .requestMatchers(HttpMethod.PUT, basePath + "/content/**").hasAnyRole("content_writer")
                    .requestMatchers(HttpMethod.DELETE, basePath + "/content/**").hasAnyRole("content_remover")
                    .requestMatchers(HttpMethod.GET, basePath + "/document/**").permitAll()
                    .requestMatchers(HttpMethod.POST, basePath + "/document/**").hasAnyRole("file_writer")
                    .requestMatchers(HttpMethod.PUT, basePath + "/document/**").hasAnyRole("file_writer")
                    .requestMatchers(HttpMethod.DELETE, basePath + "/document/**").hasAnyRole("file_remover")
                    .requestMatchers(HttpMethod.GET, basePath + "/folder/**").permitAll()
                    .requestMatchers(HttpMethod.POST, basePath + "/folder/**").hasAnyRole("directory_writer")
                    .requestMatchers(HttpMethod.PUT, basePath + "/folder/**").hasAnyRole("directory_writer")
                    .requestMatchers(HttpMethod.DELETE, basePath + "/folder/**").hasAnyRole("directory_remover")
                    .requestMatchers(HttpMethod.GET, basePath + "/menu/**").permitAll()
                    .requestMatchers(HttpMethod.POST, basePath + "/menu/**").hasAnyRole("menu_writer")
                    .requestMatchers(HttpMethod.PUT, basePath + "/menu/**").hasAnyRole("menu_writer")
                    .requestMatchers(HttpMethod.DELETE, basePath + "/menu/**").hasAnyRole("menu_remover")
                    .requestMatchers(HttpMethod.GET, basePath + "/page/**").permitAll()
                    .requestMatchers(HttpMethod.POST, basePath + "/page/**").hasAnyRole("page_writer")
                    .requestMatchers(HttpMethod.PUT, basePath + "/page/**").hasAnyRole("page_writer")
                    .requestMatchers(HttpMethod.DELETE, basePath + "/page/**").hasAnyRole("page_remover")
                    .anyRequest().authenticated();
        });
        http.oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthConverter);
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(
                httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
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
        http.oauth2Login(Customizer.withDefaults());
        http.oauth2Client(Customizer.withDefaults());
        http.logout(Customizer.withDefaults());
        return http.build();
    }

    /**
     * Configure global.
     *
     * @param auth the auth
     * @param passwordEncoder the password encoder
     * @throws Exception the exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
        auth.inMemoryAuthentication().withUser(defaultUser(securityProperties, passwordEncoder));
    }

    private UserDetails defaultUser(SecurityProperties securityProperties, PasswordEncoder passwordEncoder) {
        SecurityProperties.User user = securityProperties.getUser();
        List<String> roles = user.getRoles();
        return User.builder()
                .username(user.getName())
                .password(defaultUserPassword(user, passwordEncoder))
                .roles(StringUtils.toStringArray(roles))
                .build();
    }

    private String defaultUserPassword(SecurityProperties.User user, PasswordEncoder passwordEncoder) {
        String password = user.getPassword();
        if (user.isPasswordGenerated()) {
            log.warn(String.format(
                    "%n%nUsing generated security password: %s%n%nThis generated password is for development use"
                    + " only. "
                    + "Your security configuration must be updated before running your application in "
                    + "production.%n",
                    user.getPassword()));
        }
        if (PASSWORD_ALGORITHM_PATTERN.matcher(password).matches()) {
            return password;
        }
        if (passwordEncoder != null) {
            return passwordEncoder.encode(password);
        }
        return NOOP_PASSWORD_PREFIX + password;
    }

}
