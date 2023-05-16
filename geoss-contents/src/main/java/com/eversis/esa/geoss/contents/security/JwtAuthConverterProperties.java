package com.eversis.esa.geoss.contents.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * The type Jwt auth converter properties.
 */
@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "jwt.auth.converter")
public class JwtAuthConverterProperties {

    private String resourceId;
    private String principalAttribute;

}
