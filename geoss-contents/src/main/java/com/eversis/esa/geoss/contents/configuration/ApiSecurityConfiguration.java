package com.eversis.esa.geoss.contents.configuration;

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
                            .requestMatchers(HttpMethod.GET, basePath + "/content/**").permitAll()
                            .requestMatchers(HttpMethod.POST, basePath + "/content/**")
                            .hasAnyRole("CONTENT_WRITER", "ADMIN")
                            .requestMatchers(HttpMethod.PUT, basePath + "/content/**")
                            .hasAnyRole("CONTENT_WRITER", "ADMIN")
                            .requestMatchers(HttpMethod.DELETE, basePath + "/content/**")
                            .hasAnyRole("CONTENT_REMOVER", "ADMIN")
                            .requestMatchers(HttpMethod.GET, basePath + "/document/**").permitAll()
                            .requestMatchers(HttpMethod.POST, basePath + "/document/**")
                            .hasAnyRole("FILE_WRITER", "ADMIN")
                            .requestMatchers(HttpMethod.PUT, basePath + "/document/**")
                            .hasAnyRole("FILE_WRITER", "ADMIN")
                            .requestMatchers(HttpMethod.DELETE, basePath + "/document/**")
                            .hasAnyRole("FILE_REMOVER", "ADMIN")
                            .requestMatchers(HttpMethod.GET, basePath + "/folder/**").permitAll()
                            .requestMatchers(HttpMethod.POST, basePath + "/folder/**")
                            .hasAnyRole("DIRECTORY_WRITER", "ADMIN")
                            .requestMatchers(HttpMethod.PUT, basePath + "/folder/**")
                            .hasAnyRole("DIRECTORY_WRITER", "ADMIN")
                            .requestMatchers(HttpMethod.DELETE, basePath + "/folder/**")
                            .hasAnyRole("DIRECTORY_REMOVER", "ADMIN")
                            .requestMatchers(HttpMethod.GET, basePath + "/menu/**").permitAll()
                            .requestMatchers(HttpMethod.POST, basePath + "/menu/**").hasAnyRole("MENU_WRITER", "ADMIN")
                            .requestMatchers(HttpMethod.PUT, basePath + "/menu/**").hasAnyRole("MENU_WRITER", "ADMIN")
                            .requestMatchers(HttpMethod.DELETE, basePath + "/menu/**")
                            .hasAnyRole("MENU_REMOVER", "ADMIN")
                            .requestMatchers(HttpMethod.GET, basePath + "/page/**").permitAll()
                            .requestMatchers(HttpMethod.POST, basePath + "/page/**").hasAnyRole("PAGE_WRITER", "ADMIN")
                            .requestMatchers(HttpMethod.PUT, basePath + "/page/**").hasAnyRole("PAGE_WRITER", "ADMIN")
                            .requestMatchers(HttpMethod.DELETE, basePath + "/page/**")
                            .hasAnyRole("PAGE_REMOVER", "ADMIN")
                            .requestMatchers(HttpMethod.GET, basePath + "/site/**").permitAll()
                            .requestMatchers(HttpMethod.POST, basePath + "/site/**").hasAnyRole("SITE_WRITER", "ADMIN")
                            .requestMatchers(HttpMethod.PUT, basePath + "/site/**").hasAnyRole("SITE_WRITER", "ADMIN")
                            .requestMatchers(HttpMethod.DELETE, basePath + "/site/**")
                            .hasAnyRole("SITE_REMOVER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .anyRequest().authenticated();
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
