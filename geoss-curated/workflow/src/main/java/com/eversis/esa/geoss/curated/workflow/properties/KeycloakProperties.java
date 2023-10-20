package com.eversis.esa.geoss.curated.workflow.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Keycloak properties.
 */
@Getter
@Setter
@ConfigurationProperties("keycloak.admin")
public class KeycloakProperties {

    /**
     * The Username.
     */
    String username;

    /**
     * The Password.
     */
    String password;

    /**
     * The Server url.
     */
    String serverUrl;
}
