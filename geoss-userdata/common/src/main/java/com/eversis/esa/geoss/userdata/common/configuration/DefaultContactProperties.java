package com.eversis.esa.geoss.userdata.common.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Default contact properties.
 */
@Getter
@Setter
@ConfigurationProperties("spring.security.user")
public class DefaultContactProperties {

    private String name;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private List<String> roles = new ArrayList<>();
}
