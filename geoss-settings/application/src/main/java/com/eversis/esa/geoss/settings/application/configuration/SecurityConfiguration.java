package com.eversis.esa.geoss.settings.application.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Pattern;
import javax.sql.DataSource;

/**
 * The type Security configuration.
 */
@Log4j2
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@Configuration(proxyBeanMethods = false)
public class SecurityConfiguration {

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
     * Persistent token repository persistent token repository.
     *
     * @param dataSource the data source
     * @return the persistent token repository
     */
    @Bean
    PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl persistentTokenRepository = new JdbcTokenRepositoryImpl();
        persistentTokenRepository.setDataSource(dataSource);
        return persistentTokenRepository;
    }

    /**
     * Users user details manager.
     *
     * @param dataSource the data source
     * @param securityProperties the security properties
     * @return the user details manager
     */
    @Bean
    UserDetailsManager users(DataSource dataSource, SecurityProperties securityProperties) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select username,password,enabled, acc_locked, acc_expired, creds_expired"
                        + " from users where username = ?");
        jdbcUserDetailsManager.setCreateUserSql(
                "insert into users (username, password, enabled, acc_locked, acc_expired, creds_expired)"
                        + " values (?,?,?,?,?,?)");
        jdbcUserDetailsManager.setUpdateUserSql(
                "update users set password = ?, enabled = ?, acc_locked=?, acc_expired=?, creds_expired=?"
                        + " where username = ?");

        if (!jdbcUserDetailsManager.userExists(securityProperties.getUser().getName())) {
            jdbcUserDetailsManager.createUser(defaultUser(securityProperties));
        } else {
            jdbcUserDetailsManager.updateUser(defaultUser(securityProperties));
        }
        return jdbcUserDetailsManager;
    }

    private UserDetails defaultUser(SecurityProperties securityProperties) {
        SecurityProperties.User user = securityProperties.getUser();
        List<String> roles = user.getRoles();
        if (roles.isEmpty()) {
            roles.add(DEFAULT_ROLE_NAME);
        }
        return User.builder()
                .username(user.getName())
                .password(defaultUserPassword(user))
                .roles(StringUtils.toStringArray(roles))
                .build();
    }

    private String defaultUserPassword(SecurityProperties.User user) {
        String password = user.getPassword();
        if (user.isPasswordGenerated()) {
            log.warn(String.format(
                    "%n%nUsing generated security password: %s%n%nThis generated password is for development use only. "
                            + "Your security configuration must be updated before running your application in "
                            + "production.%n",
                    user.getPassword()));
        }
        if (PASSWORD_ALGORITHM_PATTERN.matcher(password).matches()) {
            return password;
        }
        return NOOP_PASSWORD_PREFIX + password;
    }
}
