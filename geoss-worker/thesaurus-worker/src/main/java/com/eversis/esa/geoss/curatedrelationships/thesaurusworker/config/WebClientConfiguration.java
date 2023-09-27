package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.config;

import io.netty.channel.ChannelOption;
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
     * Esa thesaurus web client web client.
     *
     * @return the web client
     * @throws SSLException the ssl exception
     */
    @Bean("esaThesaurusWebClient")
    WebClient esaThesaurusWebClient() throws SSLException {
        log.info("Configuring ESA-Thesaurus web client");

        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        HttpClient httpClient = HttpClient.create()
                .secure(sslSpec -> sslSpec.sslContext(sslContext))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    /**
     * Vocabulary server thesaurus web client web client.
     *
     * @return the web client
     */
    @Bean("vocabularyServerThesaurusWebClient")
    WebClient vocabularyServerThesaurusWebClient() {
        log.info("Configuring VocabularyServer-Thesaurus web client");
        return WebClient.create();
    }

}
