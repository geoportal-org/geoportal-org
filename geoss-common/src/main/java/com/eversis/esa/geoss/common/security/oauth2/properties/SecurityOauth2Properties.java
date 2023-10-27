package com.eversis.esa.geoss.common.security.oauth2.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Security oauth 2 properties.
 */
@Getter
@Setter
@ConfigurationProperties("spring.security.oauth2")
public class SecurityOauth2Properties {

    private boolean enabled;

    private String baseUri;

    private String clientId;

    private String clientSecret;

    private String postLogoutRedirectUri;
}
