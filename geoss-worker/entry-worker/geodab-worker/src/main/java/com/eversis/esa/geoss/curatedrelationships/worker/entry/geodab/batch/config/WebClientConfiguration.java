package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * The type Web client configuration.
 */
@Log4j2
@Configuration
class WebClientConfiguration {

    /**
     * Web client web client.
     *
     * @return the web client
     */
    @Bean
    WebClient webClient() {
        log.info("Configuring Web client");
        return WebClient.create();
    }

}
