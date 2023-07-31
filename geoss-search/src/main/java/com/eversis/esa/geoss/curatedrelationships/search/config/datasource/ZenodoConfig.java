package com.eversis.esa.geoss.curatedrelationships.search.config.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * The type Zenodo config.
 */
@Configuration
public class ZenodoConfig {

    private final Logger log = LoggerFactory.getLogger(ZenodoConfig.class);

    @Value("${datasource.zenodo.base-url}")
    private String baseUrl;

    /**
     * Zenodo client web client.
     *
     * @return the web client
     */
    @Bean("zenodoClient")
    public WebClient zenodoClient() {
        log.info("Configuring Zenodo client - baseUrl: {}", baseUrl);
        return WebClient.create(baseUrl);
    }

}
