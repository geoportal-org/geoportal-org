package com.eversis.esa.geoss.curatedrelationships.common.configuration.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * The type Reactive security configuration.
 */
@Log4j2
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@Configuration(proxyBeanMethods = false)
public class ReactiveSecurityConfiguration {

    private static final String DEFAULT_ROLE_NAME = "DEFAULT";

    private static final String NOOP_PASSWORD_PREFIX = "{noop}";

    private static final Pattern PASSWORD_ALGORITHM_PATTERN = Pattern.compile("^\\{.+}.*$");

    /**
     * Password encoder password encoder.
     *
     * @return the password encoder
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Map reactive user details service map reactive user details service.
     *
     * @param securityProperties the security properties
     * @param passwordEncoders the password encoders
     * @return the map reactive user details service
     */
    @Bean
    MapReactiveUserDetailsService mapReactiveUserDetailsService(SecurityProperties securityProperties,
            ObjectProvider<PasswordEncoder> passwordEncoders) {
        return new MapReactiveUserDetailsService(defaultUser(securityProperties, passwordEncoders));
    }

    private UserDetails defaultUser(SecurityProperties securityProperties,
            ObjectProvider<PasswordEncoder> passwordEncoders) {
        SecurityProperties.User user = securityProperties.getUser();
        List<String> roles = user.getRoles();
        if (roles.isEmpty()) {
            roles.add(DEFAULT_ROLE_NAME);
        }
        return User.builder()
                .username(user.getName())
                .password(defaultUserPassword(user, passwordEncoders))
                .roles(StringUtils.toStringArray(roles))
                .build();
    }

    private String defaultUserPassword(SecurityProperties.User user, ObjectProvider<PasswordEncoder> passwordEncoders) {
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
        PasswordEncoder passwordEncoder = passwordEncoders.getIfAvailable();
        if (passwordEncoder != null) {
            return passwordEncoder.encode(password);
        }
        return NOOP_PASSWORD_PREFIX + password;
    }
}
