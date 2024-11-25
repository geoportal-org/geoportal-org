package com.eversis.esa.geoss.personaldata.application.configuration;

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
                            .requestMatchers(HttpMethod.GET, basePath + "/comments/**")
                            .hasAnyRole("COMMENT_READER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.DELETE, basePath + "/comments/**")
                            .hasAnyRole("COMMENT_REMOVER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(basePath + "/comments/**")
                            .hasAnyRole("COMMENT_WRITER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/feedback/**")
                            .hasAnyRole("FEEDBACK_READER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.DELETE, basePath + "/feedback/**")
                            .hasAnyRole("FEEDBACK_REMOVER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.POST, basePath + "/feedback/**")
                            .permitAll();
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/highlighted-searches/search/enabled")
                            .permitAll();
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/highlighted-searches/**")
                            .hasAnyRole("HIGHLIGHTED_SEARCHES_READER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.DELETE, basePath + "/highlighted-searches/**")
                            .hasAnyRole("HIGHLIGHTED_SEARCHES_REMOVER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(basePath + "/highlighted-searches/**")
                            .hasAnyRole("HIGHLIGHTED_SEARCHES_WRITER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/saved-runs/search/current")
                            .hasAnyRole("SAVED_RUNS_MANAGER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/saved-runs/search/**")
                            .hasAnyRole("SAVED_RUNS_READER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/saved-runs")
                            .hasAnyRole("SAVED_RUNS_READER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/saved-runs/**")
                            .hasAnyRole("SAVED_RUNS_MANAGER", "SAVED_RUNS_READER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.DELETE, basePath + "/saved-runs/**")
                            .hasAnyRole("SAVED_RUNS_MANAGER", "SAVED_RUNS_REMOVER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(basePath + "/saved-runs/**")
                            .hasAnyRole("SAVED_RUNS_MANAGER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/saved-searches/search/current")
                            .hasAnyRole("SAVED_SEARCHES_MANAGER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/saved-searches/search/**")
                            .hasAnyRole("SAVED_SEARCHES_READER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/saved-searches")
                            .hasAnyRole("SAVED_SEARCHES_READER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/saved-searches/**")
                            .hasAnyRole("SAVED_SEARCHES_MANAGER", "SAVED_SEARCHES_READER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.DELETE, basePath + "/saved-searches/**")
                            .hasAnyRole("SAVED_SEARCHES_MANAGER", "SAVED_SEARCHES_REMOVER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(basePath + "/saved-searches/**")
                            .hasAnyRole("SAVED_SEARCHES_MANAGER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/surveys/**")
                            .hasAnyRole("SURVEY_READER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.DELETE, basePath + "/surveys/**")
                            .hasAnyRole("SURVEY_REMOVER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.POST, basePath + "/surveys/**")
                            .permitAll();
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/settings/search/current")
                            .hasAnyRole("USER_SETTINGS_MANAGER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/settings/search/**")
                            .hasAnyRole("USER_SETTINGS_READER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/settings")
                            .hasAnyRole("USER_SETTINGS_READER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.GET, basePath + "/settings/**")
                            .hasAnyRole("USER_SETTINGS_MANAGER", "USER_SETTINGS_READER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(HttpMethod.DELETE, basePath + "/settings/**")
                            .hasAnyRole("USER_SETTINGS_MANAGER", "USER_SETTINGS_REMOVER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers(basePath + "/settings/**")
                            .hasAnyRole("USER_SETTINGS_MANAGER", "ADMIN");
                    authorizationManagerRequestMatcherRegistry
                            .anyRequest().authenticated();
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
