package com.eversis.esa.geoss.curated.relations.configuration;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The type Relations configuration.
 */
@EnableElasticsearchRepositories(basePackages = "com.eversis.esa.geoss.curated.relations.elasticsearch.repository")
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.curated.relations.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.relations.controller",
                "com.eversis.esa.geoss.curated.relations.service.impl",
                "com.eversis.esa.geoss.curated.relations.mapper",
                "com.eversis.esa.geoss.curated.relations.elasticsearch.service.impl",
                "com.eversis.esa.geoss.curated.relations.elasticsearch.mapper"
        }
)
@PropertySource("classpath:application-relations.properties")
@Configuration(proxyBeanMethods = false)
public class RelationsConfiguration {

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
     * Keycloak client keycloak.
     *
     * @return the keycloak
     */
    @Bean
    public Keycloak keycloakRelationClient() {
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
