package com.eversis.esa.geoss.curatedrelationships.search.config.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * The type Ameri geoss ckan config.
 */
@Configuration
public class AmeriGeossCkanConfig {

    private final Logger log = LoggerFactory.getLogger(AmeriGeossCkanConfig.class);

    @Value("${datasource.amerigeoss-ckan.base-url}")
    private String baseUrl;

    /**
     * Amerigeoss ckan client web client.
     *
     * @return the web client
     */
    @Bean("amerigeossCkanClient")
    public WebClient amerigeossCkanClient() {
        log.info("Configuring AmeriGeoss CKAN client - baseUrl: {}", baseUrl);
        return WebClient.create(baseUrl);
    }

}
