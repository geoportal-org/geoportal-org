package com.eversis.esa.geoss.settings.application.configuration;

import com.eversis.esa.geoss.common.security.ApiSecurityFilterChainCustomizer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.BaseUri;
import org.springframework.http.HttpMethod;

/**
 * The type Api security configuration.
 */
@Log4j2
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ApiSecurityConfiguration {

    private final BaseUri baseUri;

    /**
     * Api security filter chain security filter chain.
     *
     * @return the security filter chain
     */
    @Bean
    ApiSecurityFilterChainCustomizer apiSecurityWebFilterChainCustomizer() {
        return http -> {
            final String basePath = baseUri.getUri().toString();
            http.securityMatcher(basePath + "/**");
            try {
                http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/api-settings/**")
                            .permitAll();
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.DELETE, basePath + "/api-settings/**")
                            .hasAnyRole("API_REMOVER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(basePath + "/api-settings/**")
                            .hasAnyRole("API_WRITER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/catalogs/**")
                            .permitAll();
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.DELETE, basePath + "/catalogs/**")
                            .hasAnyRole("CATALOG_REMOVER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(basePath + "/catalogs/**")
                            .hasAnyRole("CATALOG_WRITER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/layers/**")
                            .permitAll();
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.DELETE, basePath + "/layers/**")
                            .hasAnyRole("LAYER_REMOVER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(basePath + "/layers/**")
                            .hasAnyRole("LAYER_WRITER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/tags/**")
                            .permitAll();
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.DELETE, basePath + "/tags/**")
                            .hasAnyRole("TAG_REMOVER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(basePath + "/tags/**")
                            .hasAnyRole("TAG_WRITER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/views/**")
                            .permitAll();
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.DELETE, basePath + "/views/**")
                            .hasAnyRole("VIEW_REMOVER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(basePath + "/views/**")
                            .hasAnyRole("VIEW_WRITER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/web-settings/**")
                            .permitAll();
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.DELETE, basePath + "/web-settings/**")
                            .hasAnyRole("WEB_REMOVER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(basePath + "/web-settings/**")
                            .hasAnyRole("WEB_WRITER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/regional-settings/**")
                            .permitAll();
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(basePath + "/regional-settings/**")
                            .hasAnyRole("SETTINGS_WRITER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/sites/**")
                            .permitAll();
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.DELETE, basePath + "/sites/**")
                            // delegate authorization to method access-control
                            .authenticated();
                    authorizationManagerRequestMatcherRegistry
                            .anyRequest().authenticated();
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
