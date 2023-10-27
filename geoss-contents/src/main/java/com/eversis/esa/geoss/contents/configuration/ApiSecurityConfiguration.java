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
                            .requestMatchers(HttpMethod.POST, basePath + "/content/**").hasAnyRole("content_writer")
                            .requestMatchers(HttpMethod.PUT, basePath + "/content/**").hasAnyRole("content_writer")
                            .requestMatchers(HttpMethod.DELETE, basePath + "/content/**").hasAnyRole("content_remover")
                            .requestMatchers(HttpMethod.GET, basePath + "/document/**").permitAll()
                            .requestMatchers(HttpMethod.POST, basePath + "/document/**").hasAnyRole("file_writer")
                            .requestMatchers(HttpMethod.PUT, basePath + "/document/**").hasAnyRole("file_writer")
                            .requestMatchers(HttpMethod.DELETE, basePath + "/document/**").hasAnyRole("file_remover")
                            .requestMatchers(HttpMethod.GET, basePath + "/folder/**").permitAll()
                            .requestMatchers(HttpMethod.POST, basePath + "/folder/**").hasAnyRole("directory_writer")
                            .requestMatchers(HttpMethod.PUT, basePath + "/folder/**").hasAnyRole("directory_writer")
                            .requestMatchers(HttpMethod.DELETE, basePath + "/folder/**").hasAnyRole("directory_remover")
                            .requestMatchers(HttpMethod.GET, basePath + "/menu/**").permitAll()
                            .requestMatchers(HttpMethod.POST, basePath + "/menu/**").hasAnyRole("menu_writer")
                            .requestMatchers(HttpMethod.PUT, basePath + "/menu/**").hasAnyRole("menu_writer")
                            .requestMatchers(HttpMethod.DELETE, basePath + "/menu/**").hasAnyRole("menu_remover")
                            .requestMatchers(HttpMethod.GET, basePath + "/page/**").permitAll()
                            .requestMatchers(HttpMethod.POST, basePath + "/page/**").hasAnyRole("page_writer")
                            .requestMatchers(HttpMethod.PUT, basePath + "/page/**").hasAnyRole("page_writer")
                            .requestMatchers(HttpMethod.DELETE, basePath + "/page/**").hasAnyRole("page_remover");
                    authorizationManagerRequestMatcherRegistry
                            .anyRequest().authenticated();
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
