package com.eversis.esa.geoss.curated.workflow.configuration;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The type Workflow configuration.
 */
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.workflow.controller",
                "com.eversis.esa.geoss.curated.workflow.service.impl",
        }
)
@PropertySource("classpath:application-workflow.properties")
@Configuration(proxyBeanMethods = false)
public class WorkflowConfiguration {

    /**
     * The Username.
     */
    @Value("${keycloak.admin}")
    String username;

    /**
     * The Password.
     */
    @Value("${keycloak.admin.password}")
    String password;

    /**
     * The Server url.
     */
    @Value("${spring.security.oauth2.base.uri}")
    String serverUrl;

    /**
     * Keycloak keycloak.
     *
     * @return the keycloak
     */
    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")
                .grantType("password")
                .username(username)
                .password(password)
                .clientId("admin-cli")
                .build();
    }

}
