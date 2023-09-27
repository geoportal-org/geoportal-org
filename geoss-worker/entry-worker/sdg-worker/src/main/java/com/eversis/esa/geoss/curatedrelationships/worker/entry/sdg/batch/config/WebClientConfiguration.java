package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

/**
 * The type Web client configuration.
 */
@Log4j2
@Configuration
class WebClientConfiguration {

    /**
     * Sdg un web client web client.
     *
     * @return the web client
     */
    @Bean("sdgUnWebClient")
    WebClient sdgUnWebClient() {
        log.info("Configuring Web client");
        return WebClient.builder()
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }

    /**
     * Geo portal web client web client.
     *
     * @return the web client
     * @throws SSLException the ssl exception
     */
    @Bean("geoPortalWebClient")
    WebClient geoPortalWebClient() throws SSLException {
        log.info("Configuring geo portal Web client");

        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        HttpClient httpClient = HttpClient.create()
                .secure(sslSpec -> sslSpec.sslContext(sslContext));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

}
