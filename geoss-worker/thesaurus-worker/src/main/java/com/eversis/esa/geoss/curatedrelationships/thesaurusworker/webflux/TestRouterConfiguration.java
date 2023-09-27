package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.webflux;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * The type Test router configuration.
 */
@Log4j2
@Configuration
public class TestRouterConfiguration {

    private static Mono<ServerResponse> ping(ServerRequest request) {
        log.info("ping:pong");
        return ServerResponse.ok().bodyValue("PONG");
    }

    /**
     * Ping router function.
     *
     * @return the router function
     */
    @Bean
    RouterFunction<ServerResponse> ping() {
        return route(path("/ping"), TestRouterConfiguration::ping);
    }
}
