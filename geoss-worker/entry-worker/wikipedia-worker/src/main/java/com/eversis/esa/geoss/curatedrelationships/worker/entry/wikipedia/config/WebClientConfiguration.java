package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.config;

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
     * Wikidata sparql client web client.
     *
     * @return the web client
     */
    @Bean("wikidataSparqlClient")
    WebClient wikidataSparqlClient() {
        log.info("Configuring WikiData SPARQL web client");
        return WebClient.create();
    }

    /**
     * Wiki api web client web client.
     *
     * @return the web client
     */
    @Bean("wikiApiWebClient")
    WebClient wikiApiWebClient() {
        log.info("Configuring Wikipedia API web client");
        return WebClient.create();
    }

}
