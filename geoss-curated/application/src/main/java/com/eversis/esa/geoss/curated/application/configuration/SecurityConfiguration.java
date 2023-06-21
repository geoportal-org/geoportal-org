package com.eversis.esa.geoss.curated.application.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
     * In memory user details manager user details manager.
     *
     * @return the user details manager
     */
    @ConditionalOnProperty(prefix = "spring.security.user.details", name = "manager", havingValue = "memory")
    @Bean
    UserDetailsManager inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager();
    }

    /**
     * Users user details manager.
     *
     * @param dataSource the data source
     * @return the user details manager
     */
    @ConditionalOnProperty(prefix = "spring.security.user.details", name = "manager", havingValue = "jdbc")
    @Bean
    UserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
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
        return jdbcUserDetailsManager;
    }

    /**
     * The type User details configuration.
     */
    @RequiredArgsConstructor
    @ConditionalOnProperty(prefix = "spring.security.user.details", name = "init", havingValue = "true")
    @Configuration(proxyBeanMethods = false)
    static class UserDetailsConfiguration implements InitializingBean {

        private final SecurityProperties securityProperties;

        private final UserDetailsManager userDetailsManager;

        private final ObjectProvider<PasswordEncoder> passwordEncoders;

        @Override
        public void afterPropertiesSet() throws Exception {
            if (!userDetailsManager.userExists(securityProperties.getUser().getName())) {
                userDetailsManager.createUser(defaultUser(securityProperties));
            } else {
                userDetailsManager.updateUser(defaultUser(securityProperties));
            }
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
}
