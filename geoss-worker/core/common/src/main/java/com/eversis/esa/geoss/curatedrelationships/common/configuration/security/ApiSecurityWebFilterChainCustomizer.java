package com.eversis.esa.geoss.curatedrelationships.common.configuration.security;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;

/**
 * The interface Api security web filter chain customizer.
 */
@FunctionalInterface
public interface ApiSecurityWebFilterChainCustomizer extends Customizer<ServerHttpSecurity> {

}
