package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.configuration;

import com.eversis.esa.geoss.curatedrelationships.common.configuration.security.ApiSecurityWebFilterChainCustomizer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

/**
 * The type Reactive web security configuration.
 */
@Configuration(proxyBeanMethods = false)
public class ThesaurusReactiveWebSecurityConfiguration {

    @Value("${spring.data.rest.base-path}")
    private String basePath;

    /**
     * Api security web filter chain customizer api security web filter chain customizer.
     *
     * @return the api security web filter chain customizer
     */
    @Bean
    ApiSecurityWebFilterChainCustomizer apiSecurityWebFilterChainCustomizer() {
        return http -> {
            http.securityMatcher(ServerWebExchangeMatchers.pathMatchers(basePath + "/**"));
            http.authorizeExchange(authorizeExchangeSpec -> {
                authorizeExchangeSpec.pathMatchers(HttpMethod.POST, basePath + "/jobs/**")
                        .hasAnyRole("WORKER_MANAGER", "ADMIN");
                authorizeExchangeSpec.pathMatchers(basePath + "/jobs/**")
                        .hasAnyRole("WORKER_MANAGER", "ADMIN");
                authorizeExchangeSpec.anyExchange().authenticated();
            });
        };
    }
}
