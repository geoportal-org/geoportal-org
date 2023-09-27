package com.eversis.esa.geoss.curatedrelationships.common.configuration.security;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;

/**
 * The interface Default security web filter chain customizer.
 */
@FunctionalInterface
public interface DefaultSecurityWebFilterChainCustomizer extends Customizer<ServerHttpSecurity> {

}
