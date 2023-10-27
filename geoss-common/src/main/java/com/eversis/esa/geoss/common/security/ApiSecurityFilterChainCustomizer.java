package com.eversis.esa.geoss.common.security;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * The interface Api security filter chain customizer.
 */
@FunctionalInterface
public interface ApiSecurityFilterChainCustomizer extends Customizer<HttpSecurity> {

}
