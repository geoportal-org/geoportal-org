package com.eversis.esa.geoss.common.security;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * The interface Default security filter chain customizer.
 */
@FunctionalInterface
public interface DefaultSecurityFilterChainCustomizer extends Customizer<HttpSecurity> {

}
